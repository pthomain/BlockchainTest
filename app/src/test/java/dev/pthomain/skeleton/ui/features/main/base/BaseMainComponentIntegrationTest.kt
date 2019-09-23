package dev.pthomain.skeleton.ui.features.main.base

import dev.pthomain.skeleton.base.integration.BaseIntegrationTest

abstract class BaseMainComponentIntegrationTest<T : Any>(
    targetExtractor: (MainTestComponent) -> T
) : BaseIntegrationTest<MainTestComponent, T>(
    MainTestComponentProvider(),
    targetExtractor
)
