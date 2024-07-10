/*
 * Aurora Store
 *  Copyright (C) 2021, Rahul Kumar Patel <whyorean@gmail.com>
 *
 *  Aurora Store is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  Aurora Store is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Aurora Store.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package com.aurora.store.view.ui.commons

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.aurora.gplayapi.data.models.App
import com.aurora.gplayapi.data.models.Category
import com.aurora.store.MobileNavigationDirections
import com.google.gson.Gson
import java.lang.reflect.ParameterizedType
import javax.inject.Inject

abstract class BaseFragment<ViewBindingType : ViewBinding> : Fragment() {
    @Inject
    lateinit var gson: Gson

    private var _binding: ViewBindingType? = null
    protected val binding get() = _binding!!

    @Suppress("UNCHECKED_CAST")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val type =
            (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<ViewBindingType>
        val method = type.getMethod(
            "inflate",
            LayoutInflater::class.java,
            ViewGroup::class.java,
            Boolean::class.java
        )
        _binding = method.invoke(null, inflater, container, false) as ViewBindingType

        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    fun openDetailsFragment(packageName: String, app: App? = null) {
        findNavController().navigate(
            MobileNavigationDirections.actionGlobalAppDetailsFragment(packageName, app)
        )
    }

    fun openCategoryBrowseFragment(category: Category) {
        findNavController().navigate(
            MobileNavigationDirections.actionGlobalCategoryBrowseFragment(
                category.title,
                category.browseUrl
            )
        )
    }

    fun openStreamBrowseFragment(browseUrl: String, title: String = "") {
        if (browseUrl.lowercase().contains("expanded")) {
            findNavController().navigate(
                MobileNavigationDirections.actionGlobalExpandedStreamBrowseFragment(
                    title,
                    browseUrl
                )
            )
        } else if (browseUrl.lowercase().contains("developer")) {
            findNavController().navigate(
                MobileNavigationDirections.actionGlobalDevProfileFragment(
                    browseUrl.substringAfter("developer-"),
                    title
                )
            )
        } else {
            findNavController().navigate(
                MobileNavigationDirections.actionGlobalStreamBrowseFragment(
                    browseUrl,
                    title
                )
            )
        }
    }

    fun openEditorStreamBrowseFragment(browseUrl: String, title: String = "") {
        findNavController().navigate(
            MobileNavigationDirections.actionGlobalEditorStreamBrowseFragment(title, browseUrl)
        )
    }

    fun openScreenshotFragment(app: App, position: Int) {
        findNavController().navigate(
            MobileNavigationDirections.actionGlobalScreenshotFragment(
                position,
                app.screenshots.toTypedArray()
            )
        )
    }

    fun openAppMenuSheet(app: App) {
        findNavController().navigate(MobileNavigationDirections.actionGlobalAppMenuSheet(app))
    }
}
