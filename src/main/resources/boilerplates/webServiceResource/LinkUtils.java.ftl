package ${fullPackage};

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.LinkBuilder;
import org.springframework.hateoas.TemplateVariable;
import org.springframework.hateoas.TemplateVariables;
import org.springframework.hateoas.UriTemplate;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

public class LinkUtils {

	/**
	 * Creates {@link Link} pointing to the URI of a {@link LinkBuilder} with its query parameters expanded with
	 * the values found in a {@link HttpServletRequest}.
	 *
	 * @param linkBuilder Used to obtain the href of the link.
	 * @param uriParams Values of the Uri parameters to be included in the href of the link.
	 * @return {@link Link} with parameter values expanded.
	 */
	public static Link createLink(LinkBuilder linkBuilder, LinkedMultiValueMap<String, String> uriParams) {
		UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(linkBuilder.withSelfRel().getHref());
		String uriStr = uriBuilder
				.replaceQueryParams(uriParams)
				.build(true)
				.toUriString();
		return new Link(uriStr);
	}

	//=========================
	// URI Parameter Utilities
	//=========================

	public static LinkedMultiValueMap<String, String> getUriParamsIncludingOnly(HttpServletRequest request, String... paramsToInclude) {
		LinkedMultiValueMap<String, String> requestParams = getUriParams(request);
		LinkedMultiValueMap<String, String> result = new LinkedMultiValueMap<>();
		Arrays.asList(paramsToInclude).stream()
			.filter(paramName -> requestParams.get(paramName) != null)
			.forEach(paramName -> result.put(paramName, requestParams.get(paramName)));
		return result;
	}

	public static LinkedMultiValueMap<String, String> getUriParamsExcluding(HttpServletRequest request, String... paramsToExclude) {
		LinkedMultiValueMap<String, String> requestParams = getUriParams(request);
		Arrays.asList(paramsToExclude).forEach(requestParams::remove);
		return requestParams;
	}

	/**
	 * Returns {@link LinkedMultiValueMap} containing the parameter values of the {@link HttpServletRequest} passed
	 * as parameter.
	 *
	 * @param request Request containing the URI parameter values to be extracted.
	 * @param postProcessor Optional processor to "tune up"/filter the query parameters to be returned. Can be null.
	 * @return {@link LinkedMultiValueMap} containing the parameter values of the {@link HttpServletRequest} passed
	 * as parameter.
	 */
	public static LinkedMultiValueMap<String, String> getUriParams(HttpServletRequest request) {
		Map<String, List<String>> paramMap = request.getParameterMap().entrySet().stream()
			.collect(Collectors.toMap(Map.Entry::getKey, entry -> Arrays.asList(entry.getValue())));
		return new LinkedMultiValueMap<String, String>(paramMap);
	}

	//============================
	// Templated Links Utilities
	//============================

	public static Link createTemplatedLink(Class<?> controllerClass, String rel, String... requestParamNames) {
		return createTemplatedLink(linkTo(controllerClass), rel, requestParamNames);
	}

	public static Link createTemplatedLink(ControllerLinkBuilder linkBuilder, String rel, String... requestParamNames) {
		String uriString = linkBuilder.toUriComponentsBuilder().build().toUriString();
		return new Link(new UriTemplate(uriString, createTemplateVars(requestParamNames)), rel);
	}

	public static TemplateVariables createTemplateVars(String... requestParamNames) {
		TemplateVariable[] vars = Stream.of(requestParamNames)
				.map(name -> new TemplateVariable(name, TemplateVariable.VariableType.REQUEST_PARAM))
				.toArray(TemplateVariable[]::new);
		return new TemplateVariables(vars);
	}
}
