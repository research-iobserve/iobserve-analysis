/***************************************************************************
 * Copyright 2014 iObserve Project (http://dfg-spp1593.de/index.php?id=44)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ***************************************************************************/
package org.iobserve.monitoring.probe.j2ee;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

import org.iobserve.common.record.EJBDeployedEvent;
import org.iobserve.common.record.EJBUndeployedEvent;

import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;
import kieker.monitoring.timer.ITimeSource;

/**
 * This interceptor class is called when a EJB is deployed and undeploy. In Glassfish
 * the invocation is delayed until the first invocation of a bean happens. It also
 * might be invoked more than once, if the service calls the interceptor for
 * every instance creation once.
 *
 * The interceptor uses an deploymentId value for the {@code ejb-jar.xml} file which
 * must be defined as an {@code <eny-entry}. The two callbacks for the interceptor
 * must be defined as below.
 *
 * {@code
 * <interceptor>
 * 		<interceptor-class>org.spp.iobserve.monitoring.probe.j2ee.DeploymentInterceptor</interceptor-class>
 * 		<env-entry>
 * 			<env-entry-name>deploymentId</env-entry-name>
 * 			<env-entry-type>java.lang.String</env-entry-type>
 * 			<env-entry-value>database-access-instance-01</env-entry-value>
 * 		</env-entry>
 * 		<post-construct>
 * 		  	<lifecycle-callback-class></lifecycle-callback-class>
 * 			<lifecycle-callback-method>postConstruct</lifecycle-callback-method>
 * 		</post-construct>
 *
 * <pre-destroy>
 * 			<lifecycle-callback-class></lifecycle-callback-class>
 * 			<lifecycle-callback-method>preDestroy</lifecycle-callback-method>
 * 		</pre-destroy>
 *
 * </interceptor>
 * }
 *
 * To add the interceptor to an assembly so that it is called at runtime must be done as follows:
 *
 * {@code
 *   <assembly-descriptor>
 *   	<interceptor-binding>
 *          <ejb-name>*</ejb-name>
 *  		    <interceptor-class>org.spp.iobserve.monitoring.probe.j2ee.TraceInterceptor</interceptor-class>
 *  		    <interceptor-class>org.spp.iobserve.monitoring.probe.j2ee.DeploymentInterceptor</interceptor-class>
 *  	</interceptor-binding>
 *   </assembly-descriptor>
 * }
 *
 * @author Reiner Jung
 *
 */
@Interceptor
public class DeploymentInterceptor {
	// logger presently not used.
	// private static final Log LOG = LogFactory.getLog(DeploymentInterceptor.class);

	@Resource(name = "deploymentId")
	private String deploymentId;

	private final IMonitoringController monitoringCtrl;
	private final ITimeSource timeSource;

	public DeploymentInterceptor() {
		this.monitoringCtrl = MonitoringController.getInstance();
		this.timeSource = this.monitoringCtrl.getTimeSource();
	}

	@PostConstruct
	public Object postConstruct(final InvocationContext context) throws Exception {
		final Object result = context.proceed();
		if (this.monitoringCtrl.isMonitoringEnabled()) {
			final String signature = context.getTarget().getClass().getCanonicalName();

			// if (this.monitoringCtrl.isProbeActivated(signature)) {
			this.monitoringCtrl.newMonitoringRecord(new EJBDeployedEvent(
					this.timeSource.getTime(), signature, this.deploymentId));
			// }
		}
		return result;
	}

	@PreDestroy
	public Object preDestroy(final InvocationContext context) throws Exception {
		final Object result = context.proceed();
		if (this.monitoringCtrl.isMonitoringEnabled()) {
			final String signature = context.getTarget().getClass().getCanonicalName();
			// if (this.monitoringCtrl.isProbeActivated(signature)) {
			this.monitoringCtrl.newMonitoringRecord(new EJBUndeployedEvent(
					this.timeSource.getTime(), signature, this.deploymentId));
			// }
		}
		return result;
	}
}
