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

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.collision.PhysicsCollisionObject;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.objects.PhysicsRigidBody;
import com.jme3.collision.CollisionResults;
import com.jme3.input.ChaseCamera;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Triangle;
import com.jme3.math.Vector3f;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer;
import com.jme3.scene.control.CameraControl.ControlDirection;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;

public class HelloPinball extends SimpleApplication {


	private boolean flipping2Render;
	private boolean flippingRender;
	private Vector3f flipper2Normal;
	private Vector3f flipper1Normal;
    private boolean flip;
    private boolean flipping;
    private boolean flip2;
    private boolean flipping2;
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
	private Geometry flipper_geo;
	private Geometry flipper2_geo;
	private Geometry flipper3_geo;
	private Geometry flipper4_geo;
	private Geometry flipper5_geo;
	private Geometry flipper6_geo;

    public static void main(String[] args) {
        HelloPinball app = new HelloPinball();
        app.setShowSettings(false);
        app.setDisplayFps(false);
        app.setDisplayStatView(false);
        
        app.start();
    }

    @Override
    public void simpleInitApp() {
    	GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        settings.setFullscreen(device.isFullScreenSupported());
        setSettings(settings);
        restart();
        
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
        flipper_geo=flipper.getGeo();
		rootNode2.attachChild(flipper);

        PinballFlipper flipper2 = new PinballFlipper();
        flipper2.init(bulletAppState, mat,new Vector3f(0.3f,0,0),new Vector3f(5f, 1, -3.5f),false);
        flipper2_phy=flipper2.getPhysic();
        flipper2_geo=flipper2.getGeo();
        rootNode2.attachChild(flipper2);

        PinballFlipper flipper3 = new PinballFlipper();
        flipper3.init(bulletAppState, mat,new Vector3f(0.3f,0,0),new Vector3f(5f, 1, 2.5f),true);
        flipper3_phy=flipper3.getPhysic();
        flipper3_geo=flipper3.getGeo();
		rootNode2.attachChild(flipper3);

        PinballFlipper flipper4 = new PinballFlipper();
        flipper4.init(bulletAppState, mat,new Vector3f(0.3f,0,0),new Vector3f(5f, 1, 0.0f),false);
        flipper4_phy=flipper4.getPhysic();
        flipper4_geo=flipper4.getGeo();
        rootNode2.attachChild(flipper4);
        
        PinballFlipper flipper5 = new PinballFlipper();
        flipper5.init(bulletAppState, mat,new Vector3f(0.3f,0,0),new Vector3f(-0.75f, 1, 4.25f),true);
        flipper5_phy=flipper5.getPhysic();
        flipper5_geo=flipper5.getGeo();
		rootNode2.attachChild(flipper5);

        PinballFlipper flipper6 = new PinballFlipper();
        flipper6.init(bulletAppState, mat,new Vector3f(0.3f,0,0),new Vector3f(-0.75f, 1, 1.75f),false);
        flipper6_phy=flipper6.getPhysic();
        flipper6_geo=flipper6.getGeo();
        rootNode2.attachChild(flipper6);
		
		lanceur_geo = new PinballLauncher();
		lanceur_geo.init(bulletAppState, mat);
		rootNode2.attachChild(lanceur_geo);
		lanceur_phy=lanceur_geo.getPhysic();
		
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
					
					Vector3f flipperNormal = event.getNormalWorldOnB();
					if (nonBoule==b) flipperNormal=flipperNormal.negate();
					
					if (nonBoule.getObjectId()==lanceur_phy.getObjectId()) {
						boule_phy.setLinearVelocity(new Vector3f(-23+(float)Math.random(),0,0));
					}
					
					
//					
//		    		if (nonBoule.getObjectId()==flipper_phy.getObjectId()) {
//		    			collision1(boule_phy,flipper_phy,flipperNormal);
//		    			flipper1Normal=flipperNormal;
//		    		}
//		    		if (nonBoule.getObjectId()==flipper2_phy.getObjectId()) {
//		    			collision2(boule_phy,flipper2_phy,flipperNormal);
//		    			flipper2Normal=flipperNormal;
//		    		}
//		    		if (nonBoule.getObjectId()==flipper3_phy.getObjectId()) {
//		    			collision1(boule_phy,flipper3_phy,flipperNormal);
//		    			flipper1Normal=flipperNormal;
//		    		}
//		    		if (nonBoule.getObjectId()==flipper4_phy.getObjectId()) {
//		    			collision2(boule_phy,flipper4_phy,flipperNormal);
//		    			flipper2Normal=flipperNormal;
//		    		}
//		    		if (nonBoule.getObjectId()==flipper5_phy.getObjectId()) {
//		    			collision1(boule_phy,flipper5_phy,flipperNormal);
//		    			flipper1Normal=flipperNormal;
//		    		}
//		    		if (nonBoule.getObjectId()==flipper6_phy.getObjectId()) {
//		    			collision2(boule_phy,flipper6_phy,flipperNormal);
//		    			flipper2Normal=flipperNormal;
//		    		}

					
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
                	flipping=keyPressed;
                	flippingRender=(keyPressed?true:flippingRender);
                }
                if (name.equals("flip2")) {
                	flip2=keyPressed;
                	flipping2=keyPressed;
                	flipping2Render=(keyPressed?true:flipping2Render);
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
    		
    		
    		// check collision
    		CollisionResults results=new CollisionResults();
    		ball.getWorldBound().collideWith(flipper_geo.getWorldBound(), results);
    		if (results.size()>0 ) {
//    			collision1(boule_phy,flipper_phy,flipper1Normal);
    			Triangle tri = new Triangle();
    			flipper_geo.getMesh().getTriangle(results.getClosestCollision().getTriangleIndex(),tri);
    			collision1(boule_phy,flipper_phy,tri.getNormal());
    		}
    		ball.getWorldBound().collideWith(flipper2_geo.getWorldBound(), results);
    		if (results.size()>0 ) {
//    			collision2(boule_phy,flipper2_phy,flipper2Normal);
    			Triangle tri = new Triangle();
    			flipper2_geo.getMesh().getTriangle(results.getClosestCollision().getTriangleIndex(),tri);
    			collision2(boule_phy,flipper2_phy,tri.getNormal());
    		}
    		ball.getWorldBound().collideWith(flipper3_geo.getWorldBound(), results);
    		if (results.size()>0 ) {
//    			collision1(boule_phy,flipper3_phy,flipper1Normal);
    			Triangle tri = new Triangle();
    			flipper3_geo.getMesh().getTriangle(results.getClosestCollision().getTriangleIndex(),tri);
    			collision1(boule_phy,flipper3_phy,tri.getNormal());
       		}
    		ball.getWorldBound().collideWith(flipper4_geo.getWorldBound(), results);
    		if (results.size()>0 ) {
//    			collision2(boule_phy,flipper4_phy,flipper2Normal);
    			Triangle tri = new Triangle();
    			flipper4_geo.getMesh().getTriangle(results.getClosestCollision().getTriangleIndex(),tri);
    			collision2(boule_phy,flipper4_phy,tri.getNormal());
       		}
    		ball.getWorldBound().collideWith(flipper5_geo.getWorldBound(), results);
    		if (results.size()>0 ) {
//    			collision1(boule_phy,flipper5_phy,flipper1Normal);
    			Triangle tri = new Triangle();
    			flipper5_geo.getMesh().getTriangle(results.getClosestCollision().getTriangleIndex(),tri);
    			collision1(boule_phy,flipper5_phy,tri.getNormal());
       		}
    		ball.getWorldBound().collideWith(flipper6_geo.getWorldBound(), results);
    		if (results.size()>0 ) {
//    			collision2(boule_phy,flipper6_phy,flipper2Normal);
    			Triangle tri = new Triangle();
    			flipper6_geo.getMesh().getTriangle(results.getClosestCollision().getTriangleIndex(),tri);
    			collision2(boule_phy,flipper6_phy,tri.getNormal());
       		}
    		
    		 
    		//flip : touche appuyé
    		//flfipping : déplacement en cours (donc super launch)
    		//flippingDone : fin de déplacement (freeze)
    		
    		
    		// si je lance plus tard ça laisse bouger la bille jusqu'à ce que j'appui.
    		
    		if (flip) {
				if (flippingRender && flipper_geo_angle<=Math.PI/2+1.1f) {
	    			// ça c'est fixe.
					
					flipper_geo_angle+=0.003f;
	        		flipper_phy.setPhysicsRotation(new Quaternion().fromAngles(0,flipper_geo_angle,0));
	        		flipper3_phy.setPhysicsRotation(new Quaternion().fromAngles(0,flipper_geo_angle,0));
	        		flipper5_phy.setPhysicsRotation(new Quaternion().fromAngles(0,flipper_geo_angle,0));
        		} else {
        			flipping=false;
        		}
    		} else {
    			flipper_geo_angle=1.1f;
    			flipping=false;
    			flippingRender=false;
    			
    			//-63 =>
    	        // 180 => Math.PI
    			flipper_phy.setPhysicsRotation(new Quaternion().fromAngles(0,flipper_geo_angle,0));
    			flipper3_phy.setPhysicsRotation(new Quaternion().fromAngles(0,flipper_geo_angle,0));
    			flipper5_phy.setPhysicsRotation(new Quaternion().fromAngles(0,flipper_geo_angle,0));
    		}
			if (flip2) {
				if (flipping2Render && flipper2_geo_angle>=-Math.PI/2-1.1f) {
					// ça c'est fixe.
					flipper2_geo_angle-=0.003f;
	        		flipper2_phy.setPhysicsRotation(new Quaternion().fromAngles(0,flipper2_geo_angle,0));
	        		flipper4_phy.setPhysicsRotation(new Quaternion().fromAngles(0,flipper2_geo_angle,0));
	        		flipper6_phy.setPhysicsRotation(new Quaternion().fromAngles(0,flipper2_geo_angle,0));
        		} else {
        			flipping2=false;
        		}
    		} else {
    			flipper2_geo_angle=-1.1f;
    			flipping2=false;
    			flipping2Render=false;
    			
    			//-63 =>
    	        // 180 => Math.PI
    			flipper2_phy.setPhysicsRotation(new Quaternion().fromAngles(0,flipper2_geo_angle,0));
    			flipper4_phy.setPhysicsRotation(new Quaternion().fromAngles(0,flipper2_geo_angle,0));
    			flipper6_phy.setPhysicsRotation(new Quaternion().fromAngles(0,flipper2_geo_angle,0));
    		}
    	}
    	counter+=tpf;
    }

	void collision1(RigidBodyControl boule_phy, RigidBodyControl flipper_phy,Vector3f normal) {
		if (!flipping) {
		} else {
			// flipper_phy.getLinearVelocity() toujours à 0,0,0 j'utilise normal du coup.
			Vector3f Vab = boule_phy.getLinearVelocity().subtract(normal).add(new Vector3f(-10,0,0));
			Vab.setY(0);
			boule_phy.setLinearVelocity(Vab);
			flipping=false;
		}
	}

	void collision2(RigidBodyControl boule_phy, RigidBodyControl flipper_phy,Vector3f normal) {
		if (!flipping2) {
		} else {
			// flipper_phy.getLinearVelocity() toujours à 0,0,0 j'utilise normal du coup.
			Vector3f Vab = boule_phy.getLinearVelocity().subtract(normal).mult(5);//.add(new Vector3f(-10,0,0));
			Vab.setY(0);
			boule_phy.setLinearVelocity(Vab);
			flipping2=false;
		}
	}

}
