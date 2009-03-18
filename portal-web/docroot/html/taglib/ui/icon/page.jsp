<%
/**
 * Copyright (c) 2000-2009 Liferay, Inc. All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
%>

<%@ include file="/html/taglib/ui/icon/init.jsp" %>

<%
String onClickHtml = StringPool.BLANK;

if (method.equals("post") && (url.startsWith(Http.HTTP_WITH_SLASH) || url.startsWith(Http.HTTPS_WITH_SLASH))) {
	onClickHtml = "onclick=\"Liferay.Util.forcePost(this); return false;\"";
}

String cssClassHtml = StringPool.BLANK;

if (Validator.isNotNull(cssClass)) {
	cssClassHtml = "class=\"nobr " + cssClass + "\"";
}

String targetHtml = StringPool.BLANK;

if (Validator.isNotNull(target) && !target.equals("_self")) {
	targetHtml = "target=\"" + target + "\"";
}

if (themeDisplay.isThemeImagesFastLoad()) {
	SpriteImage spriteImage = null;
	String spriteFileName = null;

	String imageFileName = StringUtil.replace(src, "common/../", "");

	String imagesPath = theme.getContextPath() + theme.getImagesPath();

	if (imageFileName.startsWith(imagesPath)) {
		imageFileName = imageFileName.substring(imagesPath.length());

		spriteImage = theme.getSpriteImage(imageFileName);

		if (spriteImage != null) {
			spriteFileName = themeDisplay.getPathThemeImages() + spriteImage.getSpriteFileName();
		}
	}

	if (spriteImage == null) {
		Portlet portlet = (Portlet)request.getAttribute("liferay-portlet:icon_portlet:portlet");

		if (portlet == null) {
			portlet = (Portlet)request.getAttribute(WebKeys.RENDER_PORTLET);
		}

		if (portlet != null) {
			PortletApp portletApp = portlet.getPortletApp();

			imageFileName = src;

			if (portletApp.isWARFile() && imageFileName.startsWith(portlet.getContextPath())) {
				imageFileName = imageFileName.substring(portlet.getContextPath().length());
			}

			spriteImage = portletApp.getSpriteImage(imageFileName);

			if (spriteImage != null) {
				spriteFileName = portlet.getContextPath() + spriteImage.getSpriteFileName();
			}
		}
	}

	if (spriteImage != null) {
		src = themeDisplay.getPathThemeImages() + "/spacer.png";

		details += " style=\"background-image: url('" + spriteFileName + "'); background-position: 50% -" + spriteImage.getOffset() + "px; background-repeat: no-repeat; height: " + spriteImage.getHeight() + "px; width: " + spriteImage.getWidth() + "px;\"";
	}
}
%>

<c:choose>
	<c:when test="<%= (iconListIconCount != null) && ((iconListSingleIcon == null) || iconListShowWhenSingleIcon) %>">
		<li <%= cssClassHtml %>><c:if test="<%= Validator.isNotNull(url) %>"><a href="<%= url %>" <%= onClickHtml %> <%= targetHtml %>></c:if><img class="icon" src="<%= src %>" <%= details %> /><c:if test="<%= Validator.isNotNull(url) %>"></a></c:if> <c:if test="<%= Validator.isNotNull(url) %>"><a href="<%= url %>" <%= targetHtml %>></c:if><liferay-ui:message key="<%= message %>" /><c:if test="<%= Validator.isNotNull(url) %>"></a></c:if></li>
	</c:when>
	<c:when test="<%= (iconMenuIconCount != null) && ((iconMenuSingleIcon == null) || iconMenuShowWhenSingleIcon) %>">
		<li <%= cssClassHtml %>><c:if test="<%= Validator.isNotNull(url) %>"><a href="<%= url %>" <%= onClickHtml %> <%= targetHtml %>></c:if><img class="icon" src="<%= src %>" <%= details %> /> <liferay-ui:message key="<%= message %>" /><c:if test="<%= Validator.isNotNull(url) %>"></a></c:if></li>
	</c:when>
	<c:otherwise>
		<span <%= cssClassHtml %>><c:if test="<%= Validator.isNotNull(url) %>"><a href="<%= url %>" <%= onClickHtml %> <%= targetHtml %>></c:if><img class="icon" src="<%= src %>" <%= details %> /><c:if test="<%= Validator.isNotNull(url) %>"></a></c:if><c:if test="<%= label %>"> <c:if test="<%= Validator.isNotNull(url) %>"><a href="<%= url %>" <%= targetHtml %>></c:if><liferay-ui:message key="<%= message %>" /><c:if test="<%= Validator.isNotNull(url) %>"></a></c:if></c:if></span>
	</c:otherwise>
</c:choose>