/*
 * Aurora Store
 *  Copyright (C) 2021, Rahul Kumar Patel <whyorean@gmail.com>
 *  Copyright (C) 2022, The Calyx Institute
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

package com.aurora.store

import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat.Type.displayCutout
import androidx.core.view.WindowInsetsCompat.Type.ime
import androidx.core.view.WindowInsetsCompat.Type.systemBars
import androidx.lifecycle.lifecycleScope
import com.aurora.store.data.helper.UpdateHelper
import com.aurora.store.data.receiver.MigrationReceiver
import com.aurora.store.databinding.ActivityMainBinding
import com.aurora.store.util.Preferences
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var updateHelper: UpdateHelper

    private lateinit var B: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        // Check and run migrations first if required
        // This is needed thanks to OEMs breaking the MY_PACKAGE_REPLACED API
        MigrationReceiver.runMigrationsIfRequired(this)

        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        B = ActivityMainBinding.inflate(layoutInflater)
        setContentView(B.root)

        // Adjust root view's paddings for edgeToEdge display
        ViewCompat.setOnApplyWindowInsetsListener(B.root) { root, windowInsets ->
            val insets = windowInsets.getInsets(systemBars() or displayCutout() or ime())
            root.setPadding(0, insets.top, 0, 0)
            windowInsets
        }

        // Updates
        lifecycleScope.launch {
            updateHelper.updates.collectLatest { list ->
                // Handle updates if necessary
            }
        }
    }
}
