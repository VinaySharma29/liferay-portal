/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.bookmarks.uad.display;

import com.liferay.bookmarks.uad.constants.BookmarksUADConstants;
import com.liferay.bookmarks.uad.entity.BookmarksEntryUADEntity;
import com.liferay.portal.kernel.portlet.LiferayPortletRequest;
import com.liferay.portal.kernel.portlet.LiferayPortletResponse;
import com.liferay.user.associated.data.anonymizer.UADEntityAnonymizer;
import com.liferay.user.associated.data.display.UADEntityDisplay;
import com.liferay.user.associated.data.entity.UADEntity;

import java.util.Map;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author William Newbury
 */
@Component(
	immediate = true,
	property = "model.class.name=" + BookmarksUADConstants.CLASS_NAME_BOOKMARKS_ENTRY,
	service = UADEntityDisplay.class
)
public class BookmarksEntryUADEntityDisplay implements UADEntityDisplay {

	public String getApplicationName() {
		return BookmarksUADConstants.UAD_ENTITY_SET_NAME;
	}

	public String[] getDisplayFieldNames() {
		return _bookmarksEntryUADEntityDisplayHelper.getDisplayFieldNames();
	}

	public String getEditURL(
			UADEntity uadEntity, LiferayPortletRequest liferayPortletRequest,
			LiferayPortletResponse liferayPortletResponse)
		throws Exception {

		BookmarksEntryUADEntity bookmarksEntryUADEntity =
			(BookmarksEntryUADEntity)uadEntity;

		return _bookmarksEntryUADEntityDisplayHelper.getBookmarksEntryEditURL(
			bookmarksEntryUADEntity.getBookmarksEntry(), liferayPortletRequest,
			liferayPortletResponse);
	}

	public String getKey() {
		return BookmarksUADConstants.CLASS_NAME_BOOKMARKS_ENTRY;
	}

	@Override
	public Map<String, Object> getUADEntityNonanonymizableFieldValues(
		UADEntity uadEntity) {

		BookmarksEntryUADEntity bookmarksEntryUADEntity =
			(BookmarksEntryUADEntity)uadEntity;

		return _bookmarksEntryUADEntityDisplayHelper.
			getUADEntityNonanonymizableFieldValues(
				bookmarksEntryUADEntity.getBookmarksEntry());
	}

	public String getUADEntityTypeDescription() {
		return "A link to another page or website";
	}

	public String getUADEntityTypeName() {
		return "BookmarksEntry";
	}

	@Reference
	private BookmarksEntryUADEntityDisplayHelper
		_bookmarksEntryUADEntityDisplayHelper;

	@Reference(
		target = "(model.class.name=" + BookmarksUADConstants.CLASS_NAME_BOOKMARKS_ENTRY + ")"
	)
	private UADEntityAnonymizer _uadEntityAnonymizer;

}