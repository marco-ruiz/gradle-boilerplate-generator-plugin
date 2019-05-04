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

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

public class PageOfCollection<T> extends PageImpl<T> {

	private static final long serialVersionUID = 3587907037872779493L;

	private static <T> List<T> getPage(Collection<T> collection, Pageable page) {
		return collection.stream()
				.skip(page.getOffset())
				.limit(page.getPageSize())
				.collect(Collectors.toList());
	}

	public PageOfCollection(Collection<T> content, Pageable page) {
		super(getPage(content, page), page, content.size());
	}
}
