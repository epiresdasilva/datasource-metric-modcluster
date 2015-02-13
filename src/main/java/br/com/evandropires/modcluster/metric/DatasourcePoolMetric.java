package br.com.evandropires.modcluster.metric;

import java.lang.management.ManagementFactory;

import javax.management.InstanceNotFoundException;
import javax.management.MBeanServer;
import javax.management.ObjectName;

import org.jboss.modcluster.container.Engine;
import org.jboss.modcluster.load.metric.impl.AbstractLoadMetric;

public class DatasourcePoolMetric extends AbstractLoadMetric {

	public DatasourcePoolMetric() {
	}

	@Override
	public double getLoad(Engine engine) throws Exception {
		MBeanServer server = ManagementFactory.getPlatformMBeanServer();
		try {
			ObjectName datasourcePool = new ObjectName(
					"jboss.as:subsystem=datasources,data-source=MyDS,statistics=pool");
			ObjectName datasource = new ObjectName(
					"jboss.as:subsystem=datasources,data-source=MyDS");
			Integer inUseCount = (Integer) server.getAttribute(datasourcePool,
					"InUseCount");
			Integer maxPoolSize = (Integer) server.getAttribute(datasource,
					"max-pool-size");
			return inUseCount.doubleValue() / maxPoolSize.doubleValue();
		} catch (InstanceNotFoundException e) {
			return 0;
		}
	}

}
