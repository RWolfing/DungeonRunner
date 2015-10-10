package de.dungeonrunner;

import java.awt.Rectangle;

import org.jsfml.graphics.RectangleShape;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.system.Time;
import org.jsfml.system.Vector2f;

import tiled.core.Map;
import tiled.core.MapLayer;
import tiled.core.Tile;
import tiled.core.TileLayer;

public class SFMLTileRenderer extends SceneNode {

	private final Map mTiledMap;
	private RectangleShape mCacheTile = new RectangleShape();

	public SFMLTileRenderer(Map map) {
		mTiledMap = map;
	}

	@Override
	protected void drawCurrent(RenderTarget target, RenderStates states) {
		for (MapLayer layer : mTiledMap) {
			if (layer instanceof TileLayer) {
				paintTileLayer(target, states, (TileLayer) layer);
			}
		}
	}

	public void paintTileLayer(RenderTarget target, RenderStates states, TileLayer layer) {
		final int tileWidth = mTiledMap.getTileWidth();
		final int tileHeight = mTiledMap.getTileHeight();
		final Rectangle boundsInTiles = layer.getBounds();

		TextureHolder textureHolder = TextureHolder.getInstance();

		for (int x = 0; x < boundsInTiles.getWidth(); x++) {
			for (int y = 0; y < boundsInTiles.getHeight(); y++) {
				Tile tile = layer.getTileAt(x, y);
				if (tile == null) {
					continue;
				} else {
					mCacheTile.setSize(new Vector2f(tileWidth, tileHeight));
					mCacheTile.setTexture(textureHolder.getTileTexture(tile.getId()));
					mCacheTile.setPosition(x * tileWidth, y * tileHeight);
					target.draw(mCacheTile);
				}
			}
		}
	}

	@Override
	protected void updateCurrent(Time dt) {
		// TODO Auto-generated method stub

	}

}
