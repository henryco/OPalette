package net.henryco.opalette.api.glES.render.graphics.shaders.textures.extend;

import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.GLES20;

import net.henryco.opalette.api.glES.camera.Camera2D;
import net.henryco.opalette.api.glES.render.graphics.shaders.textures.OPallTexture;
import net.henryco.opalette.api.utils.GLESUtils;
import net.henryco.opalette.api.utils.lambda.consumers.OPallConsumer;
import net.henryco.opalette.api.utils.lambda.functions.OPallFunction;

/**
 * Created by HenryCo on 10/03/17.
 */

public class EdTexture extends OPallTextureExtended  {

	public final GLESUtils.Color add = new GLESUtils.Color(GLESUtils.Color.TRANSPARENT);
	public final GLESUtils.Color min = new GLESUtils.Color(GLESUtils.Color.TRANSPARENT);
	public final GLESUtils.Color max = new GLESUtils.Color(GLESUtils.Color.WHITE);

	private float addBrightness = 0;
	private float alpha = 1;


	public EdTexture(Context context) {
		this(context, Filter.LINEAR);
	}
	public EdTexture(Context context, Filter filter) {
		super(context, filter, OPallTexture.DEF_SHADER+".vert", FRAG_DIR);
	}
	public EdTexture(Bitmap image, Context context) {
		this(image, context, Filter.LINEAR);
	}
	public EdTexture(Bitmap image, Context context, Filter filter) {
		super(image, context, filter, OPallTexture.DEF_SHADER+".vert", FRAG_DIR);
	}
	public EdTexture() {
		super(OPallTexture.DEFAULT_VERT_FILE, FRAG_FILE);
	}
	public EdTexture(Bitmap image) {
		this(image, Filter.LINEAR);
	}
	public EdTexture(Bitmap image, Filter filter) {
		super(image, filter, OPallTexture.DEFAULT_VERT_FILE, FRAG_FILE);
	}


	public EdTexture minColor(OPallConsumer<GLESUtils.Color> min) {
		min.consume(this.min);
		return this;
	}
	public EdTexture maxColor(OPallConsumer<GLESUtils.Color> max) {
		max.consume(this.max);
		return this;
	}
	public EdTexture addColor(OPallConsumer<GLESUtils.Color> add) {
		add.consume(this.add);
		return this;
	}
	public EdTexture brightness(OPallFunction<Float, Float> brightness) {
		addBrightness = brightness.apply(addBrightness);
		return this;
	}
	public EdTexture alpha(OPallFunction<Float, Float> alpha) {
		this.alpha = alpha.apply(this.alpha);
		return this;
	}


	public float getBrightness() {
		return addBrightness;
	}

	public float getAlpha() {
		return alpha;
	}


	@Override
	protected void render(int program, Camera2D camera) {
		GLES20.glUniform1f(GLES20.glGetUniformLocation(program, "u_alpha"), alpha);
		GLES20.glUniform1f(GLES20.glGetUniformLocation(program, "u_addBrightness"), addBrightness);
		GLES20.glUniform3f(GLES20.glGetUniformLocation(program, "u_addColor"), add.r, add.g, add.b);
		GLES20.glUniform3f(GLES20.glGetUniformLocation(program, "u_minColor"), min.r, min.g, min.b);
		GLES20.glUniform3f(GLES20.glGetUniformLocation(program, "u_maxColor"), max.r, max.g, max.b);
	}



	private static final String FRAG_DIR = OPallTexture.FRAG_DIR+"/EditableDefault.frag";
	private static final String FRAG_FILE =
			"precision mediump float;\n" +
					"\n" +
					"varying vec4 v_Position;\n" +
					"varying vec4 v_WorldPos;\n" +
					"varying vec2 v_TexCoordinate;\n" +
					"\n" +
					"uniform sampler2D u_Texture0;\n" +
					"\n" +
					"\n" +
					"uniform vec3 u_addColor;\n" +
					"uniform vec3 u_minColor;\n" +
					"uniform vec3 u_maxColor;\n" +
					"uniform float u_alpha;\n" +
					"uniform float u_addBrightness;\n" +
					"\n" +
					"\n" +
					"void main() {\n" +
					"\n" +
					"    vec4 color = texture2D(u_Texture0, v_TexCoordinate).rgba;\n" +
					"\n" +
					"    if (color.a != 0.) {\n" +
					"        color.r = max(min(u_maxColor.r, color.r + u_addBrightness + u_addColor.r), u_minColor.r);\n" +
					"        color.g = max(min(u_maxColor.g, color.g + u_addBrightness + u_addColor.g), u_minColor.g);\n" +
					"        color.b = max(min(u_maxColor.b, color.b + u_addBrightness + u_addColor.b), u_minColor.b);\n" +
					"        color.a = u_alpha;\n" +
					"    }\n" +
					"\n" +
					"    gl_FragColor = color;\n" +
					"}";

}
