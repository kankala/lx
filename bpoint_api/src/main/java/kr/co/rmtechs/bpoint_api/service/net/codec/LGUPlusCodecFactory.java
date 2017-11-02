package kr.co.rmtechs.bpoint_api.service.net.codec;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

public class LGUPlusCodecFactory implements ProtocolCodecFactory {
    private final LGUPlusEncoder encoder;
    private final LGUPlusDecoder decoder;

    /**
     * Creates a new instance with the {@link ClassLoader} of
     * the current thread.
     */
    public LGUPlusCodecFactory() {
    	encoder = new LGUPlusEncoder();
    	decoder = new LGUPlusDecoder();
    }
 
    public ProtocolEncoder getEncoder(IoSession session) {
        return encoder;
    }

    public ProtocolDecoder getDecoder(IoSession session) {
        return decoder;
    }
}