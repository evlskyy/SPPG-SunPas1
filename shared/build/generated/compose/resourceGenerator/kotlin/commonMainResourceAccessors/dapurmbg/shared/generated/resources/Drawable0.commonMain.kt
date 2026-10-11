@file:OptIn(InternalResourceApi::class)

package dapurmbg.shared.generated.resources

import kotlin.OptIn
import kotlin.String
import kotlin.collections.MutableMap
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.InternalResourceApi
import org.jetbrains.compose.resources.ResourceItem

private const val MD: String = "composeResources/dapurmbg.shared.generated.resources/"

internal val Res.drawable.ic_app_logo: DrawableResource by lazy {
      DrawableResource("drawable:ic_app_logo", setOf(
        ResourceItem(setOf(), "${MD}drawable/ic_app_logo.png", -1, -1),
      ))
    }

@InternalResourceApi
internal fun _collectCommonMainDrawable0Resources(map: MutableMap<String, DrawableResource>) {
  map.put("ic_app_logo", Res.drawable.ic_app_logo)
}
