/*
 * (C) Copyright 2016 Code-House and others.
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
 */
package org.code_house.openhab.itest;

import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.*;

import static org.junit.Assert.*;

import static org.ops4j.pax.exam.CoreOptions.*;

import java.util.EnumSet;

import javax.inject.Inject;

import org.apache.karaf.features.FeaturesService;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.MavenUtils;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.options.MavenArtifactUrlReference;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerClass;

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerClass.class)
public class FeaturesIntegrationTest {

    private final static EnumSet<FeaturesService.Option> INSTALL_OPTIONS = EnumSet.of(FeaturesService.Option.Verbose);

    @Inject
    private FeaturesService features;

    @Configuration
    public Option[] config() {
        String featuresUrl = maven("org.code-house.openhab", "features").classifier("features").type("xml").versionAsInProject().getURL();
        String karafVersion = MavenUtils.getArtifactVersion("org.apache.karaf", "apache-karaf");
        MavenArtifactUrlReference frameworkURL = maven("org.apache.karaf", "apache-karaf").type("zip").version(karafVersion);

        return new Option[]{
            karafDistributionConfiguration().karafVersion(karafVersion).frameworkUrl(frameworkURL),
            editConfigurationFileExtend("etc/org.apache.karaf.features.cfg", "featuresRepositories", "," + featuresUrl)
        };
    }

    @Test
    @Ignore
    public void testInstallOpenHabCXF() throws Exception {
        features.installFeature("openhab-cxf", INSTALL_OPTIONS);

        assertFeatureInstalled("openhab-cxf");
    }

    private void assertFeatureInstalled(String name) throws Exception {
        assertTrue("Feature " + name + " should be installed", features.isInstalled(features.getFeature(name)));
    }

}
