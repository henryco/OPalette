/*
 *   /*
 *    * Copyright (C) Henryk Timur Domagalski
 *    *
 *    * Licensed under the Apache License, Version 2.0 (the "License");
 *    * you may not use this file except in compliance with the License.
 *    * You may obtain a copy of the License at
 *    *
 *    *      http://www.apache.org/licenses/LICENSE-2.0
 *    *
 *    * Unless required by applicable law or agreed to in writing, software
 *    * distributed under the License is distributed on an "AS IS" BASIS,
 *    * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    * See the License for the specific language governing permissions and
 *    * limitations under the License.
 *
 */

package net.henryco.opalette.api.utils.bounds;

import net.henryco.opalette.api.utils.GLESUtils;
import net.henryco.opalette.api.utils.Utils;
import net.henryco.opalette.api.utils.bounds.consumer.BoundsConsumer;
import net.henryco.opalette.api.utils.bounds.observer.OPallBoundsHolder;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.Arrays;

/**
 * Created by HenryCo on 23/02/17.
 */

public class Bounds2D implements OPallBounds <Bounds2D> {


	public Bounds2D() {
	}
	public Bounds2D(Bounds2D other) {
		set(other);
	}


	public synchronized Bounds2D set(Bounds2D other) {

		x = other.x;
		y = other.y;
		width = other.width;
		height = other.height;
		scale = other.scale;
		def_width = other.def_width;
		def_height = other.def_height;
		vertices = other.getVertices();
		order = other.getOrder();
		holder.updateBounds();
		generateOrderBuffer();
		return this;
	}

	public synchronized float[] calculate(float dimX, float dimY) {
		float[] verts = Utils.arrayFlatCopy(vertices);
		if (width != 0 && height != 0 && dimX != 0 && dimY != 0) {
			float sc_x = width / dimX;
			float sc_y = height / dimY;
			float tr_x = 2 * x / dimX;
			float tr_y = 2 * y / dimY;
			for (int i = 0; i < verts.length - 1; i+= 2) {
				verts[i] = sc_x * scale * (verts[i] + 1) - 1 + tr_x;
				verts[i+1] = sc_y * scale * (verts[i+1] + 1) - 1 + tr_y;
			}
		}
		return verts;
	}

	private float x = 0;
	private float y = 0;
	private float width = 0;
	private float height = 0;
	private float scale = 1;
	private float def_width = 0;
	private float def_height = 0;

	private float[] vertices;
	private short[] order;

	public FloatBuffer vertexBuffer;
	public ShortBuffer orderBuffer;
	private OPallBoundsHolder holder = OPallBoundsHolder.proxyHolder;

	public synchronized Bounds2D setBounds(float x, float y, float w, float h, float scale) {
		this.x = x;
		this.y = y;
		this.width = w;
		this.height = h;
		this.scale = scale;
		holder.updateBounds();
		return this;
	}
	public synchronized Bounds2D setBounds(float x, float y, float w, float h){
		return setBounds(x, y, w, h, scale);
	}
	public synchronized Bounds2D setX(float x) {
		return setPosition(x, y);
	}
	public synchronized Bounds2D setY(float y) {
		return setPosition(x, y);
	}
	public synchronized Bounds2D setPosition(float x, float y) {
		return setBounds(x, y, width, height, scale);
	}
	public synchronized Bounds2D setWidth(float w) {
		return setBounds(x, y, w, height, scale);
	}
	public synchronized Bounds2D setHeight(float h) {
		return setBounds(x, y, width, h, scale);
	}
	public synchronized Bounds2D setSize(float w, float h) {
		return setBounds(x, y, w, h, scale);
	}
	public synchronized Bounds2D setScale(float scale) {
		return setBounds(x, y, width, height, scale);
	}
	public synchronized Bounds2D setDefWidth(float w) {
		this.def_width = w;
		return this;
	}
	public synchronized Bounds2D setDefHeight(float h) {
		this.def_height = h;
		return this;
	}
	public synchronized Bounds2D setDefSize(float w, float h) {
		return setDefWidth(w).setDefHeight(h);
	}
	public synchronized Bounds2D setUniSize(float w, float h) {
		return setSize(w, h).setDefSize(w, h);
	}
	public synchronized Bounds2D resetBounds(boolean full) {
		generateVertexBuffer(0, 0);
		width = full ? 0 : def_width;
		height = full ? 0 : def_height;
		scale = 1;
		x = 0;
		y = 0;
		return this;
	}

	public synchronized float getX() {
		return x;
	}
	public synchronized float getY() {
		return y;
	}
	public synchronized float getWidth(){
		return width;
	}
	public synchronized float getHeight(){
		return height;
	}
	public synchronized float getDef_width() {
		return def_width;
	}
	public synchronized float getDef_height() {
		return def_height;
	}
	public synchronized float getScale(){
		return scale;
	}
	public synchronized Bounds2D resetBounds() {
		return resetBounds(false);
	}

	public synchronized float[] getVertices() {
		return Utils.arrayFlatCopy(vertices);
	}
	public synchronized short[] getOrder() {
		return Utils.arrayFlatCopy(order);
	}
	public synchronized Bounds2D setVertices(float[] vertices) {
		this.vertices = vertices;
		generateVertexBuffer(0, 0);
		return this;
	}
	public synchronized Bounds2D setVerticesOnly(float[] vertices) {
		this.vertices = vertices;
		return this;
	}
	public synchronized Bounds2D setOrder(short[] order) {
		this.order = order;
		generateOrderBuffer();
		return this;
	}

	public synchronized int getVertexCount() {
		return order.length;
	}

	public synchronized Bounds2D generateVertexBuffer(float dimX, float dimY) {
		vertexBuffer = GLESUtils.createFloatBuffer(calculate(dimX, dimY));
		return this;
	}
	public synchronized Bounds2D generateOrderBuffer() {
		orderBuffer = GLESUtils.createShortBuffer(order);
		return this;
	}

	@Override
	public synchronized Bounds2D apply(BoundsConsumer<Bounds2D> p) {
		p.boundApply(this);
		return this;
	}

	@Override
	public synchronized Bounds2D setHolder(OPallBoundsHolder<Bounds2D> holder) {
		this.holder = holder;
		return this;
	}


	@Override
	public String toString() {
		return "Bounds2D{" +
				"x=" + x +
				", y=" + y +
				", width=" + width +
				", height=" + height +
				", scale=" + scale +
				", def_width=" + def_width +
				", def_height=" + def_height +
				", vertices=" + Arrays.toString(vertices) +
				", order=" + Arrays.toString(order) +
				", vertexBuffer=" + vertexBuffer +
				", orderBuffer=" + orderBuffer +
				", holder=" + holder +
				'}';
	}
}
