package geometry.primitive;

import geometry.Intersection;
import geometry.Point3D;
import geometry.Vector3D;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import material.MattMaterial;
import raytracer.Ray;

public class WaveFront extends Primitive {
	private ArrayList<Model> models = new ArrayList<Model>();

	public WaveFront(String filename) {
		super(new MattMaterial(Color.RED));

		String objectName = null;
		String textureFile = null;
		ArrayList<Point3D> vertices = new ArrayList<Point3D>();
		ArrayList<Vector3D> normals = new ArrayList<Vector3D>();
		ArrayList<Face> faces = new ArrayList<Face>();

		String line;

		try (
				InputStream fis = new FileInputStream(filename);
				InputStreamReader isr = new InputStreamReader(fis);
				BufferedReader br = new BufferedReader(isr);
				) {
			while ((line = br.readLine()) != null) {
				String words[] = line.split("\\s+");	// \\s == [\\t\\n\\x0B\\f\\r]

				/* Object declaration */
				if (words[0].equals("o")) {
					name = words[1];
				}

				/* Group declaration */
				/*
				else if (words[0].equals("g")) {
					if (groupName != null) {
						groups.add(new Group(groupName, vertices, normals, faces));

						vertices = new ArrayList<Point3D>();
						normals = new ArrayList<Vector3D>();
						faces = new ArrayList<Face>();
					}
					groupName = words[1];
					for (int i = 2; i < words.length; i++)
						groupName += " " + words[i];
				}
				 */

				/* MTL texture file declaration */
				else if (words[0].equals("usemtl")) {
					textureFile = words[1];
				}

				/* Vertex declaration */
				else if (words[0].equals("v")) {
					vertices.add(new Point3D(Double.parseDouble(words[1]),
							Double.parseDouble(words[2]), Double.parseDouble(words[3])));
				}

				/* Normal declaration */
				else if (words[0].equals("vn")) {
					normals.add(new Vector3D(Double.parseDouble(words[1]),
							Double.parseDouble(words[2]), Double.parseDouble(words[3])));
				}

				/* Texture declaration */
				else if (words[0].equals("vt")) {
					// TODO: manage textures
				}

				/* Face declaration */
				else if (words[0].equals("f")) {
					Face face = new Face();
					for (int i = 1; i < words.length; i++) {
						if (i >= 5) {
							System.err.println("Can not create a face with more than 4 vertices");
							break;
						}
						face.addVertex(words[i]);
					}
					faces.add(face);
				}
			}
			models.add(new Model(objectName, vertices, normals, faces));

		} catch (IOException e) {
			System.err.println("Error: Could not read file " + filename);
		}
	}

	@Override
	public Vector3D getNormal(Intersection intersection) {
		return models.get(intersection.getGroupId()).getNormal(intersection);
	}

	@Override
	public Intersection intersect(Ray ray) {
		Intersection closest = null;

		for (int i = 0; i < models.size(); i++) {
			// Intersection inter = MollerTrumbore(i, ray);
			Intersection intersection = models.get(i).intersect(ray);
			if (intersection != null)
				if (closest == null) {
					closest = intersection;
					closest.setGroupId(i);
				}
				else if (intersection.getDistance() < closest.getDistance()) {
					closest = intersection;
					closest.setGroupId(i);
				}
		}
		return closest;
	}

	@Override
	public Color getColor(Intersection intersection) {
		return models.get(intersection.getGroupId()).getColor(intersection);
	}

	public void resize(double ratio) {
		for (Model g : models)
			g.resize(ratio);
	}

}
