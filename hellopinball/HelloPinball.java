/*
 * Copyright (c) 2009-2020 jMonkeyEngine
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'jMonkeyEngine' nor the names of its contributors
 *   may be used to endorse or promote products derived from this software
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package jme3test.animation.hellopinball;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.collision.PhysicsCollisionObject;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.input.ChaseCamera;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer;
import com.jme3.scene.control.CameraControl.ControlDirection;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;

public class HelloPinball extends SimpleApplication {

    private boolean flip;
    private boolean flip2;
    private float flipper2_geo_angle;
    private float flipper_geo_angle;
    private ChaseCamera chaser;
    private CameraNode camNode;
	private BulletAppState bulletAppState;
	private RigidBodyControl flipper_phy;
	private RigidBodyControl flipper2_phy;
	private RigidBodyControl flipper3_phy;
	private RigidBodyControl flipper4_phy;
	private RigidBodyControl flipper5_phy;
	private RigidBodyControl flipper6_phy;
	private PinballLauncher lanceur_geo;
	private RigidBodyControl lanceur_phy;
	private RigidBodyControl boule_phy;
	private PinballBall ball;

    public static void main(String[] args) {
        HelloPinball app = new HelloPinball();
        app.setShowSettings(false);
        app.setDisplayFps(false);
        app.setDisplayStatView(false);
        app.start();
    }

    @Override
    public void simpleInitApp() {
    	

        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);
        

        createScene();
        camNode = new CameraNode("Motion cam", cam);
        camNode.setControlDir(ControlDirection.SpatialToCamera);
        camNode.setEnabled(false);

        rootNode.attachChild(camNode);

        flyCam.setEnabled(false);
        chaser = new ChaseCamera(cam, rootNode);
        chaser.registerWithInput(inputManager);
        chaser.setSmoothMotion(true);
        chaser.setMaxDistance(50);
        chaser.setDefaultDistance(15);
        chaser.setDefaultVerticalRotation(1.0f);
        initInputs();
    }

    private void createScene() {
        Material mat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        mat.setFloat("Shininess", 1f);
        mat.setBoolean("UseMaterialColors", true);
        mat.setColor("Ambient", ColorRGBA.Black);
        mat.setColor("Diffuse", ColorRGBA.DarkGray);
        mat.setColor("Specular", ColorRGBA.White.mult(0.6f));
        Material matSoil = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        matSoil.setBoolean("UseMaterialColors", true);
        matSoil.setColor("Ambient", ColorRGBA.Gray);
        matSoil.setColor("Diffuse", ColorRGBA.Gray);
        matSoil.setColor("Specular", ColorRGBA.Black);
        
        Material matTex = new Material(assetManager,"Common/MatDefs/Light/Lighting.j3md");//Common/MatDefs/Misc/Unshaded.j3md");
        Texture cube1Tex = assetManager.loadTexture(
                "Models/Teapot/macadamRotate.png");
        
        cube1Tex.setWrap(Texture.WrapMode.Repeat);
        
//        matTex.setTexture("ColorMap", cube1Tex);
        matTex.setTexture("DiffuseMap", cube1Tex);
        
        
        PinballTopNode rootNode2 = new PinballTopNode();
        rootNode.attachChild(rootNode2);
        rootNode2.init(assetManager, bulletAppState, matTex);
        
        
        ball = new PinballBall();
        ball.init(bulletAppState, mat);
        boule_phy=ball.getPhysic();
        rootNode.attachChild(ball);
        
        PinballFlipper flipper = new PinballFlipper();
        flipper.init(bulletAppState, mat,new Vector3f(0.3f,0,0),new Vector3f(5f, 1, -1.0f),true);
        flipper_phy=flipper.getPhysic();
		rootNode2.attachChild(flipper);

        PinballFlipper flipper2 = new PinballFlipper();
        flipper2.init(bulletAppState, mat,new Vector3f(0.3f,0,0),new Vector3f(5f, 1, -3.5f),false);
        flipper2_phy=flipper2.getPhysic();
        rootNode2.attachChild(flipper2);

        PinballFlipper flipper3 = new PinballFlipper();
        flipper3.init(bulletAppState, mat,new Vector3f(0.3f,0,0),new Vector3f(5f, 1, 2.5f),true);
        flipper3_phy=flipper3.getPhysic();
		rootNode2.attachChild(flipper3);

        PinballFlipper flipper4 = new PinballFlipper();
        flipper4.init(bulletAppState, mat,new Vector3f(0.3f,0,0),new Vector3f(5f, 1, 0.0f),false);
        flipper4_phy=flipper4.getPhysic();
        rootNode2.attachChild(flipper4);
        
        PinballFlipper flipper5 = new PinballFlipper();
        flipper5.init(bulletAppState, mat,new Vector3f(0.3f,0,0),new Vector3f(-0.75f, 1, 4.25f),true);
        flipper5_phy=flipper5.getPhysic();
		rootNode2.attachChild(flipper5);

        PinballFlipper flipper6 = new PinballFlipper();
        flipper6.init(bulletAppState, mat,new Vector3f(0.3f,0,0),new Vector3f(-0.75f, 1, 1.75f),false);
        flipper6_phy=flipper6.getPhysic();
        rootNode2.attachChild(flipper6);
		
		lanceur_geo = new PinballLauncher();
		lanceur_geo.init(bulletAppState, mat);
		rootNode2.attachChild(lanceur_geo);
		lanceur_phy=lanceur_geo.getPhysic();
		
		Mesh mesh = new Mesh();
		mesh.setMode(Mesh.Mode.Lines);
		mesh.setBuffer(VertexBuffer.Type.Position, 3, new float[]{ 0, 0, 0, 0, 10, 0});
		mesh.setBuffer(VertexBuffer.Type.Index, 2, new short[]{ 0, 1 });
		Geometry geo = new Geometry("line", mesh);
		geo.setMaterial(mat);
		rootNode.attachChild(geo);
		
		 bulletAppState.getPhysicsSpace().addCollisionListener(new PhysicsCollisionListener() {
				
				@Override
				public void collision(PhysicsCollisionEvent event) {
					PhysicsCollisionObject a = event.getObjectA();
					PhysicsCollisionObject b = event.getObjectB();
					PhysicsCollisionObject nonBoule = null;
					
					
					
					if (b.getObjectId() == boule_phy.getObjectId()) {
						nonBoule=a;
					}
					if (a.getObjectId() == boule_phy.getObjectId()) {
						nonBoule=b;
					}
					
					Vector3f normal = event.getNormalWorldOnB();
					if (nonBoule==b) normal=normal.negate();
					
					if (nonBoule.getObjectId()==lanceur_phy.getObjectId()) {
						boule_phy.setLinearVelocity(new Vector3f(-23,0,0));
					}
					if (nonBoule.getObjectId()==flipper_phy.getObjectId()) {
						collision(boule_phy,flipper_phy,normal);
					}
					if (nonBoule.getObjectId()==flipper2_phy.getObjectId()) {
						collision(boule_phy,flipper2_phy,normal);
					}
					if (nonBoule.getObjectId()==flipper3_phy.getObjectId()) {
						collision(boule_phy,flipper3_phy,normal);
					}
					if (nonBoule.getObjectId()==flipper4_phy.getObjectId()) {
						collision(boule_phy,flipper4_phy,normal);
					}
					if (nonBoule.getObjectId()==flipper5_phy.getObjectId()) {
						boule_phy.setLinearVelocity(new Vector3f(-10,0,0));
						collision(boule_phy,flipper5_phy,normal);
					}
					if (nonBoule.getObjectId()==flipper6_phy.getObjectId()) {
						collision(boule_phy,flipper6_phy,normal);
					}
				}

				private void collision(RigidBodyControl boule_phy, RigidBodyControl flipper_phy,Vector3f normal) {
					// flipper_phy.getLinearVelocity() toujours à 0,0,0 j'utilise normal du coup.
					Vector3f Vab = boule_phy.getLinearVelocity().subtract(normal).add(new Vector3f(-10,0,0));
					Vab.setY(0);
					boule_phy.setLinearVelocity(Vab);
				}
			});
        
		 
		 
        Geometry soil = new Geometry("soil", new Box(50, 1, 50));
        soil.setLocalTranslation(0, -1, 0);
        soil.setMaterial(matSoil);
        RigidBodyControl solPhysic = new RigidBodyControl(0f);
        soil.addControl(solPhysic);
        bulletAppState.getPhysicsSpace().add(solPhysic);
        rootNode.attachChild(soil);
        
        DirectionalLight light = new DirectionalLight();
        light.setDirection(new Vector3f(0, -1, 0).normalizeLocal());
        light.setColor(ColorRGBA.White.mult(1.5f));
        rootNode.addLight(light);
    }
    
    

    private void initInputs() {
    	inputManager.addMapping("relancer", new KeyTrigger(KeyInput.KEY_SPACE));
    	inputManager.addMapping("flip", new KeyTrigger(KeyInput.KEY_F));
        inputManager.addMapping("flip2", new KeyTrigger(KeyInput.KEY_H));
        ActionListener acl = new ActionListener() {

			@Override
            public void onAction(String name, boolean keyPressed, float tpf) {
                if (name.equals("flip")) {
                	flip=keyPressed;
                }
                if (name.equals("flip2")) {
                	flip2=keyPressed;
                }
                if (name.equals("relancer")) {
                	ball.getPhysic().setPhysicsLocation(new Vector3f(0,10,-5.5f));
                	ball.getPhysic().setLinearVelocity(new Vector3f(0,0,0));
                	ball.getPhysic().setAngularVelocity(new Vector3f(0,0,0));
                }
            }
        };

        inputManager.addListener(acl, /*"display_hidePath", "play_stop", "SwitchPathInterpolation", "tensionUp", "tensionDown", */"flip","flip2","relancer");
    }
    float counter=0;
    @Override
    public void simpleUpdate(float tpf) {
    	if (Math.floor(counter/(10f*25.0f))<=Math.floor(counter/(10*25.0f)+tpf/(10f*25.0f))) {
    		
    		if (flip) {
    			if (flipper_geo_angle<=Math.PI/2+1.1f) {
	    			// ça c'est fixe.
	    			flipper_geo_angle+=0.003f;
	        		flipper_phy.setPhysicsRotation(new Quaternion().fromAngles(0,flipper_geo_angle,0));
	        		flipper3_phy.setPhysicsRotation(new Quaternion().fromAngles(0,flipper_geo_angle,0));
	        		flipper5_phy.setPhysicsRotation(new Quaternion().fromAngles(0,flipper_geo_angle,0));
        		}
    		} else {
    			flipper_geo_angle=1.1f;
    			
    			//-63 =>
    	        // 180 => Math.PI
    			flipper_phy.setPhysicsRotation(new Quaternion().fromAngles(0,flipper_geo_angle,0));
    			flipper3_phy.setPhysicsRotation(new Quaternion().fromAngles(0,flipper_geo_angle,0));
    			flipper5_phy.setPhysicsRotation(new Quaternion().fromAngles(0,flipper_geo_angle,0));
    		}
			if (flip2) {
    			// ça c'est fixe.
				if (flipper2_geo_angle>=-Math.PI/2-1.1f) {
					flipper2_geo_angle-=0.003f;
	        		flipper2_phy.setPhysicsRotation(new Quaternion().fromAngles(0,flipper2_geo_angle,0));
	        		flipper4_phy.setPhysicsRotation(new Quaternion().fromAngles(0,flipper2_geo_angle,0));
	        		flipper6_phy.setPhysicsRotation(new Quaternion().fromAngles(0,flipper2_geo_angle,0));
        		}
    		} else {
    			flipper2_geo_angle=-1.1f;
    			//-63 =>
    	        // 180 => Math.PI
    			flipper2_phy.setPhysicsRotation(new Quaternion().fromAngles(0,flipper2_geo_angle,0));
    			flipper4_phy.setPhysicsRotation(new Quaternion().fromAngles(0,flipper2_geo_angle,0));
    			flipper6_phy.setPhysicsRotation(new Quaternion().fromAngles(0,flipper2_geo_angle,0));
    		}
    	}
    	counter+=tpf;
    }
}
