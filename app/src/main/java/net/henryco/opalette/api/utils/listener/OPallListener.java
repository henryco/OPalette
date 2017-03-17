package net.henryco.opalette.api.utils.listener;

import net.henryco.opalette.api.utils.lambda.consumers.OPallConsumer;

/**
 * Created by HenryCo on 17/03/17.
 */

public interface OPallListener<T> {

	void onOPallAction(OPallConsumer<T> consumer);

	interface OPallMultiListener {
		void onOPallMultiAction(OPallConsumer<Object> consumer, Class c);
	}

}

