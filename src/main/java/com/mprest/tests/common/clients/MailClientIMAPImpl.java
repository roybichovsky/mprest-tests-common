package com.mprest.tests.common.clients;

import com.mprest.tests.common.data.MailMessage;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;

import javax.mail.*;
import javax.mail.internet.ContentType;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.ParseException;
import javax.mail.search.AndTerm;
import javax.mail.search.ComparisonTerm;
import javax.mail.search.ReceivedDateTerm;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.stream.Stream;

@Slf4j
public class MailClientIMAPImpl implements MailClient {

    private static final String storeType = "imap";

    private final String host;
    private final String user;
    private final String password;

    public MailClientIMAPImpl(String host, String user, String password) {
        this.host = host;
        this.user = user;
        this.password = password;
    }

    @Override
    public Optional<MailMessage[]> getMailMessages(String folderName, Instant startDate, Instant endDate, int recordsLimit) {
        Future<Optional<MailMessage[]>> futureResult = getMailMessagesAsync(folderName, startDate, endDate, recordsLimit);
        return extractOptionalAsyncResult(futureResult, "Failed to request mail messages", Duration.ofMinutes(1));
    }

    @Override
    public Future<Optional<MailMessage[]>> getMailMessagesAsync(String folderName, Instant startDate, Instant endDate, int recordsLimit) {
        return this.supplyAsync(() -> {
            Store store = getStore();
            if (store == null) return Optional.empty();

            try {
                store.connect(this.host, this.user, this.password);
                Folder emailFolder = store.getFolder(folderName);
                emailFolder.open(Folder.READ_ONLY);
                AndTerm timeBackTerm = getDateTerm(startDate, endDate);
                Message[] messages = emailFolder.search(timeBackTerm);

                Optional<MailMessage[]> optionalMailMessages = collectMessages(messages);
                if (!optionalMailMessages.isPresent()) {
                    return Optional.empty();
                }

                MailMessage[] mailMessages = optionalMailMessages.get();
                Arrays.sort(mailMessages, (o1, o2) -> o2.getReceivedDate().compareTo(o1.getReceivedDate()));
                if (recordsLimit <= 0) {
                    return optionalMailMessages;
                } else {
                    int newRecordsLimit = recordsLimit >= messages.length ? messages.length : recordsLimit;
                    MailMessage[] latestMessages = Arrays.copyOfRange(mailMessages, 0, newRecordsLimit);
                    return Optional.ofNullable(latestMessages);
                }
            } catch (MessagingException e) {
                log.error("Failed getting mail messages", e);
                return Optional.empty();
            } finally {
                try {
                    store.close();
                } catch (MessagingException e) {
                    log.error("Failed closing mail store", e);
                }
            }
        });
    }

    private Optional<MailMessage[]> collectMessages(Message[] messages) {
        MailMessage mailMessage = null;
        MailMessage[] mailMessages = new MailMessage[messages.length];
        for (int i = 0, n = messages.length; i < n; i++) {
            Message message = messages[i];
            String result = null;
            try {
                MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
                result = getTextFromMimeMultipart(mimeMultipart);
                mailMessage = MailMessage.builder()
                        .subject(message.getSubject())
                        .body(result)
                        .from(message.getFrom()[0].toString())
                        .receivedDate(message.getReceivedDate().toInstant())
                        .build();
                mailMessages[i] = mailMessage;
            } catch (Exception e) {
                log.error("Failed collecting mail messages", e);
            }
        }
        return Optional.of(mailMessages);
    }

    private Store getStore() {
        Properties properties = new Properties();
        properties.put("mail." + this.storeType + ".host", host);
        properties.put("mail." + this.storeType + ".port", "995");
        properties.put("mail." + this.storeType + ".starttls.enable", "true");
        Session emailSession = Session.getDefaultInstance(properties);
        try {
            return emailSession.getStore(this.storeType + "s");
        } catch (NoSuchProviderException e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    private AndTerm getDateTerm(Instant beginDate, Instant endDate) {
        Calendar endDateMaxTime = Calendar.getInstance();
        endDateMaxTime.setTime(Date.from(endDate));
        endDateMaxTime.add(Calendar.DAY_OF_MONTH, 1);
        return new AndTerm(new ReceivedDateTerm(ComparisonTerm.LT, endDateMaxTime.getTime()), new ReceivedDateTerm(ComparisonTerm.GT, Date.from(beginDate)));
    }

    private String getTextFromMimeMultipart(MimeMultipart mimeMultipart) throws MessagingException {
        int count = 0;
        try {
            count = mimeMultipart.getCount();
        } catch (MessagingException e) {
            log.error("getTextFromMimeMultipart(), MessagingException", e);
            return "";
        }
        if (count == 0) {
            log.error("Multipart with no body parts not supported.");
            return "";
        }
        boolean multipartAlt = false;
        try {
            multipartAlt = new ContentType(mimeMultipart.getContentType()).match("multipart/alternative");
        } catch (ParseException e) {
            log.error("Parser exception", e);
        }
        if (multipartAlt)
            // alternatives appear in an order of increasing
            // faithfulness to the original content. Customize as req'd.
            return getTextFromBodyPart(mimeMultipart.getBodyPart(count - 1));
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < count; i++) {
            BodyPart bodyPart = mimeMultipart.getBodyPart(i);
            result.append(getTextFromBodyPart(bodyPart));
        }
        return result.toString();
    }

    private String getTextFromBodyPart(BodyPart bodyPart) {
        String result = "";
        boolean isMimeTypeTextPlain = false;
        boolean isMimeTypeTextHtml = false;
        Object content = null;
        try {
            isMimeTypeTextPlain = bodyPart.isMimeType("text/plain");
            isMimeTypeTextHtml = bodyPart.isMimeType("text/html");
            content = bodyPart.getContent();
        } catch (Exception e) {
            log.error("Exception while getting mail body content", e);
        }
        if (isMimeTypeTextPlain) {
            result = (String) content;
        } else if (isMimeTypeTextHtml) {
            String html = (String) content;
            Document doc = org.jsoup.Jsoup.parse(html);
            if (doc != null) {
                result = doc.text();
            }
        } else if (content instanceof MimeMultipart) {
            try {
                result = getTextFromMimeMultipart((MimeMultipart) content);
            } catch (Exception e) {
                log.error("Exception while parsing MIME mail body content", e);
            }
        }
        return result;
    }

    private <T> Future<T> supplyAsync(Supplier<T> supplier) {
        return CompletableFuture
                .supplyAsync(supplier)
                .thenApply(t -> t);
    }

    protected <T> Optional<T> extractOptionalAsyncResult(Future<Optional<T>> futureResult, String errorMessage, Duration timeout) {
        try {
            return futureResult.get(timeout.getSeconds(), TimeUnit.SECONDS);
        } catch (Throwable err) {
            log.error(errorMessage, err);
            return Optional.empty();
        }
    }
}
