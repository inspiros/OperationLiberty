package com.hust.utils.algebra;

import java.util.ArrayList;
import java.util.Vector;

import Jama.Matrix;

public class JacobianMatrix {

	public ArrayList<float[]> rows = new ArrayList<>();

	public void addColumn(FloatVector3 col) {
		rows.add(col.toArray());
	}

	public void addColumn(FloatVector3 rotationAxis, FloatVector3 endEffector, FloatVector3 jointStartPoint) {
		rows.add(FloatVector3.crossProduct(rotationAxis, endEffector.sub(jointStartPoint)).toArray());
	}

	public Vector<Float> pseudoInverseMul(FloatVector3 v) {
		Vector<Float> res = new Vector<>();

		double[][] jamatConstructor = new double[rows.size()][3];
		for (int i = 0; i < rows.size(); i++) {
			jamatConstructor[i][0] = rows.get(i)[0];
			jamatConstructor[i][1] = rows.get(i)[1];
			jamatConstructor[i][2] = rows.get(i)[2];
		}
		Matrix jamat = new Matrix(jamatConstructor);
		Matrix trans = jamat.transpose();
		jamat = (trans.times(jamat)).inverse().times(trans);

		for (int i = 0; i < jamat.getColumnDimension(); i++) {
			res.addElement((float) (jamat.get(0, i) * v.x + jamat.get(1, i) * v.y + jamat.get(2, i) * v.z));
		}

		return res;
	}

	public Vector<Float> mul(FloatVector3 v) {
		Vector<Float> res = new Vector<>();
		for (float[] col : rows) {
			res.add(new Float(col[0] * v.x + col[1] * v.y + col[2] * v.z));
		}
		return res;
	}
}
