package jme3test.animation.hellopinball;

import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;

public class PinballLauncher extends Geometry {
	private static final float LANCEUR_X = 8f;
	
	RigidBodyControl lanceur_phy;
	
	public PinballLauncher() {
		super("launcher", new Box(0.25f, 1f, 0.25f));
	}
	
	public void init(BulletAppState bulletAppState, Material mat) {
		setMaterial(mat);
		setLocalTranslation(LANCEUR_X-4f, 1, -5.25f);
		lanceur_phy = new RigidBodyControl(0f);
		addControl(lanceur_phy);
        bulletAppState.getPhysicsSpace().add(lanceur_phy);
	}
	
	public RigidBodyControl getPhysic() {
		return lanceur_phy;
	}

}
