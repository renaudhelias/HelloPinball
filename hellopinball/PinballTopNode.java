package jme3test.animation.hellopinball;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.util.BufferUtils;
import com.jme3.scene.VertexBuffer;
import com.jme3.scene.VertexBuffer.Format;
import com.jme3.scene.VertexBuffer.Usage;

public class PinballTopNode extends Node {
	private static final float ANGLE = -0.13f;
//	private static final float ANGLE = 5f;
	/*
	 * 7.5 degre -> 180
	 * 0.13          -> PI
	 */
	public PinballTopNode() {
		super("PinballTopNode");
		
		// pb : rotation at center of the node
	    setLocalRotation(new Quaternion().fromAngles(0,0,ANGLE));
		setLocalTranslation(0, 1, 0);

	}
	public void init(AssetManager assetManager,BulletAppState bulletAppState,Material mat) {
		
		
		Spatial cercueil = assetManager.loadModel("Models/Teapot/macadam.obj");
      
		attachChild(cercueil);
		
		cercueil.setLocalRotation(new Quaternion().fromAngles(0,0,(float)Math.PI/2));

		cercueil.setMaterial(mat);
      if (cercueil instanceof Geometry) {
    	  System.out.println("geometry");
      }
      if (cercueil instanceof Node) {
    	  System.out.println("Node");
      }
      cercueil.setLocalScale(2);
      ((Geometry)cercueil).getMesh().scaleTextureCoordinates(new Vector2f(4.0f,4.0f));
      transformUV((Geometry)cercueil,new Vector2f(1.0f,1.0f),new Vector2f(0.5f,0.0f));
      
      RigidBodyControl teapotPhysic = new RigidBodyControl(0f);
      cercueil.addControl(teapotPhysic);
      bulletAppState.getPhysicsSpace().add(teapotPhysic);
      

	}
	
	public static void transformUV(Geometry geo, Vector2f uvScale, Vector2f uvOffset) {
		List<Float> uvs = new ArrayList<Float>();
		Mesh mesh = geo.getMesh();
		FloatBuffer tb = mesh.getFloatBuffer(VertexBuffer.Type.TexCoord);
		while (tb.hasRemaining())
		{
		uvs.add(tb.get());
		uvs.add(tb.get());
		}
		FloatBuffer newUVs = BufferUtils.createVector2Buffer(uvs.size() * 2);
		for (int i = 0; i < uvs.size(); i += 2)
		{
		float u = uvs.get(i);
		float v = uvs.get(i + 1);
		u *= uvScale.x;
		v *= uvScale.y;
		u += uvOffset.x;
		v += uvOffset.y;
		newUVs.put(u);
		newUVs.put(v);
		}
		VertexBuffer tvb = new VertexBuffer(VertexBuffer.Type.TexCoord);
		newUVs.flip();
		tvb.setupData(Usage.Static, 2, Format.Float, tb);
		mesh.setBuffer(VertexBuffer.Type.TexCoord, 2, newUVs);
	}
	
}
