/*
 * Axelor Business Solutions
 *
 * Copyright (C) 2005-2026 Axelor (<http://axelor.com>).
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.axelor.apps.base.service.advancedExport;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.axelor.apps.base.AxelorException;
import com.axelor.apps.base.db.AdvancedExport;
import org.junit.jupiter.api.Test;

class AdvancedExportServiceImplTest {

  @Test
  void testGetExportLanguage_schedulerContextUsesAdvancedExportLanguage() throws AxelorException {
    AdvancedExport advancedExport = new AdvancedExport();
    advancedExport.setLanguage("fr");

    String language =
        new TestableAdvancedExportServiceImpl(null).resolveValidatedLanguage(advancedExport);

    assertEquals("fr", language);
  }

  @Test
  void testGetExportLanguage_usesAuthenticatedUserLanguageAsFallback() throws AxelorException {
    AdvancedExport advancedExport = new AdvancedExport();
    advancedExport.setLanguage(null);

    String language =
        new TestableAdvancedExportServiceImpl("en").resolveValidatedLanguage(advancedExport);

    assertEquals("en", language);
  }

  @Test
  void testGetExportLanguage_throwsWhenNoLanguageInSchedulerContext() {
    AdvancedExport advancedExport = new AdvancedExport();
    advancedExport.setLanguage(null);

    assertThrows(
        AxelorException.class,
        () -> new TestableAdvancedExportServiceImpl(null).resolveValidatedLanguage(advancedExport));
  }

  private static class TestableAdvancedExportServiceImpl extends AdvancedExportServiceImpl {

    private final String currentUserLanguage;

    private TestableAdvancedExportServiceImpl(String currentUserLanguage) {
      this.currentUserLanguage = currentUserLanguage;
    }

    @Override
    protected String getCurrentUserLanguage() {
      return currentUserLanguage;
    }

    private String resolveValidatedLanguage(AdvancedExport advancedExport) throws AxelorException {
      return getAdvancedExportLanguage(advancedExport);
    }
  }
}
