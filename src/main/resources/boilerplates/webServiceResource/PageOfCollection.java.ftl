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
