package net.henryco.opalette.utils.lambda.functions;

/**
 * Created by HenryCo on 23/02/17.
 */
@FunctionalInterface
public interface OPallFunction <R, T>{
	R apply (T t);
}
