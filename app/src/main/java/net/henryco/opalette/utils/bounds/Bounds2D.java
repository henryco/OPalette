package net.henryco.opalette.utils.bounds;

import net.henryco.opalette.utils.GLESUtils;
import net.henryco.opalette.utils.Utils;
import net.henryco.opalette.utils.bounds.consumer.BoundsConsumer;
import net.henryco.opalette.utils.bounds.observer.OPallBoundsHolder;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * Created by HenryCo on 23/02/17.
 */

public class Bounds2D implements OPallBounds <Bounds2D> {


	public float[] calculate(float dimX, float dimY) {
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

	public Bounds2D setBounds(float x, float y, float w, float h, float scale) {
		this.x = x;
		this.y = y;
		this.width = w;
		this.height = h;
		this.scale = scale;
		holder.updateBounds();
		return this;
	}
	public Bounds2D setBounds(float x, float y, float w, float h){
		return setBounds(x, y, w, h, scale);
	}
	public Bounds2D setPosition(float x, float y) {
		return setBounds(x, y, width, height, scale);
	}
	public Bounds2D setWidth(float w) {
		return setBounds(x, y, w, height, scale);
	}
	public Bounds2D setHeight(float h) {
		return setBounds(x, y, width, h, scale);
	}
	public Bounds2D setSize(float w, float h) {
		return setBounds(x, y, w, h, scale);
	}
	public Bounds2D setScale(float scale) {
		return setBounds(x, y, width, height, scale);
	}
	public Bounds2D setDefWidth(float w) {
		this.def_width = w;
		return this;
	}
	public Bounds2D setDefHeight(float h) {
		this.def_height = h;
		return this;
	}
	public Bounds2D setDefSize(float w, float h) {
		return setDefWidth(w).setDefHeight(h);
	}
	public Bounds2D setUniSize(float w, float h) {
		return setSize(w, h).setDefSize(w, h);
	}
	public Bounds2D resetBounds(boolean full) {
		generateVertexBuffer(0, 0);
		width = full ? 0 : def_width;
		height = full ? 0 : def_height;
		scale = 1;
		x = 0;
		y = 0;
		return this;
	}

	public float getWidth(){
		return width;
	}
	public float getHeight(){
		return height;
	}
	public float getDef_width() {
		return def_width;
	}
	public float getDef_height() {
		return def_height;
	}

	public Bounds2D resetBounds() {
		return resetBounds(false);
	}

	public float[] getVertices() {
		return Utils.arrayFlatCopy(vertices);
	}
	public short[] getOrder() {
		return Utils.arrayFlatCopy(order);
	}
	public Bounds2D setVertices(float[] vertices) {
		this.vertices = vertices;
		generateVertexBuffer(0, 0);
		return this;
	}
	public Bounds2D setOrder(short[] order) {
		this.order = order;
		generateOrderBuffer();
		return this;
	}

	public int getVertexCount() {
		return order.length;
	}

	public Bounds2D generateVertexBuffer(float dimX, float dimY) {
		vertexBuffer = GLESUtils.createFloatBuffer(calculate(dimX, dimY));
		return this;
	}
	public Bounds2D generateOrderBuffer() {
		orderBuffer = GLESUtils.createShortBuffer(order);
		return this;
	}

	@Override
	public Bounds2D apply(BoundsConsumer<Bounds2D> p) {
		p.boundApply(this);
		return this;
	}

	@Override
	public Bounds2D setHolder(OPallBoundsHolder<Bounds2D> holder) {
		this.holder = holder;
		return this;
	}
}
