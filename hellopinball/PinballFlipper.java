package jme3test.animation.hellopinball;

import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;

public class PinballFlipper extends Node {
//	private float brick_geo_angle;
	private Geometry brick_geo;
	private RigidBodyControl brick_phy;
	public PinballFlipper() {
		super("noeud");
	}
	
	public void init(BulletAppState bulletAppState, Material mat,Vector3f geoTranslation, Vector3f nodeTranslation,boolean isGauche) {
		// ?,hauteur,?
		Box box = new Box(0.6f, 1, 0.10f);
        box.scaleTextureCoordinates(new Vector2f(1f,0.5f));
        brick_geo = new Geometry("brick", box);
        brick_geo.setMaterial(mat);
        
        
        
        /** Make brick physical with a mass > 0.0f. */
		brick_phy = new RigidBodyControl(0f);
        /** Position the brick geometry  */
		brick_geo.setLocalTranslation(geoTranslation);
		setLocalTranslation(nodeTranslation);
        /** Add physical brick to physics space. */
        brick_geo.addControl(brick_phy);
        addControl(brick_phy);
        attachChild(brick_geo);
		bulletAppState.getPhysicsSpace().add(brick_phy);
		
//		brick_phy.setKinematic(false);
		
		//-63 =>
        // 180 => Math.PI
        if (!isGauche) {
        	brick_phy.setPhysicsRotation(new Quaternion().fromAngles(0,-1.1f,0));
        } else {
        	brick_phy.setPhysicsRotation(new Quaternion().fromAngles(0,1.1f,0));
        }
	}
	
	public RigidBodyControl getPhysic() {
		return brick_phy;
	}

	public Geometry getGeo(){
		return brick_geo;
	}
}
