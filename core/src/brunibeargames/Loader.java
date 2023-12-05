package brunibeargames;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.TextureLoader.TextureParameter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;

public class Loader
{
	static public Loader instance;
	AssetManager assetManager;
	Texture texMap;
	String strMapName;
	public Loader()
	{
		instance = this;
		assetManager = new AssetManager();
		TextureParameter param = new TextureParameter();
		param.minFilter = TextureFilter.Linear;
		/**
		 *  Get appropriate map
		 */
		int maxSize = Gdx.gl20.GL_MAX_TEXTURE_SIZE;
		Gdx.app.log("Loader", "GL20-max-texture size="+ maxSize);

		if (Gdx.gl30 != null) {
			Gdx.app.log("Loader", "GL30 Used");
		}
		strMapName = ""
				+ "map/borodinoprod.jpg";

//		if ((maxSize > 3699))
//			//|| 	Gdx.app.getType() == ApplicationType.Desktop)
// 
//		{
//			Gdx.app.log("Screen", "Using Big Map");
//			strMapName = "/map/map.png";
//		}
		assetManager.load(strMapName, Texture.class, param);

	}
	public Texture instanceGetMap()
	{
		assetManager.finishLoadingAsset(strMapName);
		texMap = assetManager.get(strMapName,Texture.class);
		return texMap;
	}


	

}
