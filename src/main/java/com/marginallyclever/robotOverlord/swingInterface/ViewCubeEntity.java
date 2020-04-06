package com.marginallyclever.robotOverlord.swingInterface;


import javax.vecmath.Matrix4d;
import javax.vecmath.Vector3d;

import com.jogamp.opengl.GL2;
import com.marginallyclever.convenience.MatrixHelper;
import com.marginallyclever.robotOverlord.RobotOverlord;
import com.marginallyclever.robotOverlord.entity.Entity;
import com.marginallyclever.robotOverlord.entity.scene.PoseEntity;
import com.marginallyclever.robotOverlord.entity.scene.modelEntity.ModelEntity;

public class ViewCubeEntity extends Entity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected ModelEntity model = new ModelEntity();
	
    public ViewCubeEntity() {
    	super();
    	setName("ViewCube");
    	model.setModelFilename("/viewCube.obj");
    	model.getMaterial().setTextureFilename("/images/viewCube.png");
    	model.getMaterial().setDiffuseColor(1, 1, 1, 1);
    	model.getMaterial().setAmbientColor(1, 1, 1, 1);
    	model.getMaterial().setLit(false);
    }
		
	public void render(GL2 gl2) {
		RobotOverlord ro = (RobotOverlord)getRoot();
		ViewportEntity cameraView = ro.viewport;
		
		gl2.glClear(GL2.GL_DEPTH_BUFFER_BIT);

		gl2.glEnable(GL2.GL_DEPTH_TEST);
		gl2.glEnable(GL2.GL_CULL_FACE);
		
		gl2.glBlendFunc(GL2.GL_SRC_ALPHA,GL2.GL_ONE_MINUS_SRC_ALPHA);
		
		gl2.glMatrixMode(GL2.GL_MODELVIEW);
		gl2.glPushMatrix();
			gl2.glLoadIdentity();
			cameraView.renderShared(gl2);
			
			PoseEntity camera = cameraView.getAttachedTo();
			Matrix4d m = camera.getPoseWorld();
			Vector3d p = camera.getPosition();
			Vector3d vx = MatrixHelper.getXAxis(m);
			Vector3d vy = MatrixHelper.getYAxis(m);
			Vector3d vz = MatrixHelper.getZAxis(m);
			double fovRadians = Math.toRadians(cameraView.fieldOfView.get());
			//System.out.println(Math.sin(fovRadians)+"\t"+Math.cos(fovRadians));
			double ar = cameraView.getAspectRatio();
			vz.scale((-cameraView.canvasHeight*2)*Math.cos(fovRadians));
			vx.scale((cameraView.canvasWidth/2-20)*ar);
			vy.scale((cameraView.canvasHeight/2-20));
			p.add(vx);
			p.add(vy);
			p.add(vz);
			
			gl2.glTranslated(p.x, p.y, p.z);
			gl2.glScaled(30, 30, 30);

	    	model.getMaterial().setLit(false);
			model.render(gl2);

			gl2.glDisable(GL2.GL_LIGHTING);
			gl2.glDisable(GL2.GL_COLOR_MATERIAL);
			gl2.glColor4d(1,1,1,1);
			
			gl2.glDisable(GL2.GL_TEXTURE_2D);
			
			// the big lines
			gl2.glLineWidth(4);
			gl2.glPushMatrix();
				gl2.glTranslated(-1.05,-1.05,-0.95);
				gl2.glBegin(GL2.GL_LINES);
				gl2.glColor3d(1, 0, 0);		gl2.glVertex3d(0, 0, 0);		gl2.glVertex3d(2.5, 0, 0);
				gl2.glColor3d(0, 1, 0);		gl2.glVertex3d(0, 0, 0);		gl2.glVertex3d(0, 2.5, 0);
				gl2.glColor3d(0, 0, 1);		gl2.glVertex3d(0, 0, 0);		gl2.glVertex3d(0, 0, 2.5);
				gl2.glEnd();
			gl2.glPopMatrix();
			gl2.glLineWidth(1);
						
		gl2.glPopMatrix();
	}
}