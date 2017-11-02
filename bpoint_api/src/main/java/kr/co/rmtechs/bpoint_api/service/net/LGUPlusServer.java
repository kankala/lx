package kr.co.rmtechs.bpoint_api.service.net;

import java.net.InetSocketAddress;

import org.apache.log4j.Logger;
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.DatagramSessionConfig;
import org.apache.mina.transport.socket.nio.NioDatagramAcceptor;

import kr.co.rmtechs.bpoint_api.service.net.codec.LGUPlusCodecFactory;

public class LGUPlusServer {
	public static final int DEFAULT_PORT			= 8007;
	public static final int DEFAULT_IDLE_TIME		= 60;
	public static final int DEFAULT_PARSER			= 1;
	
	private static Logger logger = Logger.getLogger(LGUPlusServer.class);
	
	public static LGUPlusServer SERVER;
	public static LGUPlusServer getInstance() {
		return LGUPlusServer.getInstance(DEFAULT_PORT);
	}

	public static LGUPlusServer getInstance(int port) {
		try {
			if(LGUPlusServer.SERVER == null)
				LGUPlusServer.SERVER = new LGUPlusServer(port, DEFAULT_IDLE_TIME);
		}catch(Exception ex) {
			logger.error("SERVER START Error - " + ex.toString());
			ex.printStackTrace();
		}
		
		return LGUPlusServer.SERVER;
	}	
	
	private NioDatagramAcceptor acceptor;
	
	private int port = 0;
	private int idle = 0;
	
	public LGUPlusServer(int port, int idle) throws Exception {
		if(port == 0)
			this.port = DEFAULT_PORT;
		else 
			this.port = port;
		if(idle == 0)
			this.idle = DEFAULT_IDLE_TIME;
		else
			this.idle = idle;
		
		initServer();
	}
	
	public void initServer() throws Exception {
		acceptor = new NioDatagramAcceptor();
		DefaultIoFilterChainBuilder chain = acceptor.getFilterChain();
		DatagramSessionConfig dcfg = acceptor.getSessionConfig();
		dcfg.setReuseAddress(true);

//		IoServiceMBean serviceManager = new IoServiceMBean(acceptor);
//		MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
//		ObjectName name = new ObjectName(acceptor.getClass().getPackage().getName() +
//										":type=acceptor,name=" + acceptor.getClass().getSimpleName());
//		mbs.registerMBean(serviceManager, name);
		
//		MdcInjectionFilter mdc = new MdcInjectionFilter();
//		chain.addLast("mdc", mdc);
		

		chain.addLast("codec", new ProtocolCodecFilter(new LGUPlusCodecFactory()));
		acceptor.setHandler(new LGUPlusUDPHandler());

		logger.debug("[SERVER] Server idle time setting : " + getIdle());
		acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, getIdle());
		acceptor.bind(new InetSocketAddress(getPort()));
		
		logger.debug("[SERVER] Server started in port " + getPort());
		System.out.println("[SERVER] Server started in port " + getPort());
	}
	
	/**
	 * Close NIO Socket
	 * @throws Exception
	 */
	public void close() throws Exception {
		acceptor.getFilterChain().remove("codec");
		
		acceptor.unbind();
		acceptor.dispose();
		acceptor = null;
		
		logger.debug("SERVER STOP");
	}
	
	public void restart() throws Exception {
		close();

		Thread.sleep(1000L);
		
		initServer();
	}
	
	public void setPort(int port) {
		this.port = port;
	}

	public void setIdle(int idle) {
		this.idle = idle;
	}	
	
	public int getIdle() {
		return idle;
	}
	
	public int getPort() {
		return port;
	}
	
	
	public NioDatagramAcceptor getAcceptor() {
		return this.acceptor;
	}
	
	/**
	 * IP의 접속여부
	 * @param ip
	 * @return
	 */
	public boolean isConnected(String ip) {
		InetSocketAddress addr;
		for(IoSession session : acceptor.getManagedSessions().values()) {
			addr = (InetSocketAddress)session.getRemoteAddress();
			if(addr.getAddress().getHostAddress().equals(ip)) {
				return true;
			}
		}
		
		return false;
	}

}
