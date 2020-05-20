package jme3test.animation.hellopinball;

import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Sphere;

public class PinballBall extends Geometry {
	
	private RigidBodyControl boule2_phy;
	public PinballBall() {
		super("boule2", new Sphere(32, 32, 0.2f, true, false));
	}
	public void init(BulletAppState bulletAppState, Material mat) {
		/** Create a brick geometry and attach to scene graph. */
        setMaterial(mat);
		/** Position the brick geometry  */
        setLocalTranslation(new Vector3f(0,10,-5.5f));
        /** Make brick physical with a mass > 0.0f. */
        // 80gramme (unité 1kg)
        boule2_phy = new RigidBodyControl(0.08f);
        /** Add physical brick to physics space. */
        addControl(boule2_phy);
        bulletAppState.getPhysicsSpace().add(boule2_phy);
	}
	public RigidBodyControl getPhysic() {
		return boule2_phy;
	}
}
