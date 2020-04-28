package com.mprest.tests.common.clients;

import com.mprest.tests.common.data.NetworkConstraint;
import com.mprest.tests.common.data.assets.Asset;
import com.mprest.tests.common.data.assets.AssetGeoCoordinate;

import java.util.Optional;
import java.util.concurrent.Future;

public interface AssetsClient {
    Optional<Asset> getAsset(String assetId, Class<?> clazz);
    Future<Optional<Asset>> getAssetAsync(String assetId, Class<?> clazz);

    Optional<AssetGeoCoordinate> getAssetGeo(String assetId);
    Future<Optional<AssetGeoCoordinate>> getAssetGeoAsync(String assetId);

    Optional<NetworkConstraint> getAssetNetworkConstraint(String assetId);
    Future<Optional<NetworkConstraint>> getAssetNetworkConstraintAsync(String assetId);
}