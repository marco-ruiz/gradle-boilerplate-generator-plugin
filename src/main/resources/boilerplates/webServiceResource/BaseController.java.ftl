/*
 * Copyright 2002-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ${fullPackage};

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class BaseController {

	@Value("${r"${server.contextPath}"}")
	protected String contextPath;

	/**
	 * Creates a common {@link ResponseEntity} with:
	 * <ul>
	 * <li>{@link HttpStatus#OK}</li>
	 * <li>Body equals to the {@link ResourceSupport} passed as parameter</li>
	 * <li>Location header equal to the {@code href} of the {@code ResourceSupport}'s self link ({@link Link#REL_SELF})
	 * - if present.</li>
	 * </ul>
	 *
	 * @param resource Resource to be used as a body
	 * @return Generated {@code ResponseEntity}
	 */
	public static ResponseEntity<ResourceSupport> createStandardResponse(ResourceSupport resource) {
		try {
			URI location = URI.create(resource.getLink(Link.REL_SELF).getHref());
			return ResponseEntity.ok().location(location).body(resource);
		} catch(Exception e) {
			return ResponseEntity.ok(resource);
		}
	}

	/**
	 * Path relative to the application's context path. Given a URI, this method return everything after the value
	 * of the {@code server.contextPath} property
	 *
	 * @param uri URI to be parsed
	 * @return Path relative to the application's context path
	 */
	public String getRelativePath(String uri) {
		return uri.contains(contextPath) ? uri.split(contextPath, 2)[1] : uri;
	}
}
