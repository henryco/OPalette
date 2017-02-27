package net.henryco.opalette.api.utils.requester;

import net.henryco.opalette.api.utils.lambda.consumers.OPallConsumer;

/**
 * Created by HenryCo on 27/02/17.
 */

public class Request {

	public static final class Destination {
		private long[] dest;
		private long[] excp;

		private Destination() {
			all();
		}
		public Destination id(long ... id) {
			dest = new long[id.length];
			System.arraycopy(id, 0, dest, 0, id.length);
			return this;
		}
		public Destination all() {
			dest = new long[0];
			excp = new long[0];
			return this;
		}
		public Destination except(long ... id) {
			excp = new long[id.length];
			System.arraycopy(id, 0, excp, 0, id.length);
			return this;
		}
		public long[] getDestinations(){
			long[] dst = new long[dest.length];
			System.arraycopy(dest, 0, dst, 0, dst.length);
			return dst;
		}
		public long[] getExceptions(){
			long[] ecp = new long[excp.length];
			System.arraycopy(excp, 0, ecp, 0, ecp.length);
			return ecp;
		}

	}
	public final Destination destination = new Destination();
	public Request destination(OPallConsumer<Destination> d) {
		d.consume(destination);
		return this;
	}



}
