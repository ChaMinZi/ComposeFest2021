/*
 * Copyright (C) 2020 The Android Open Source Project
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

package com.codelabs.state.todo

import android.util.Log
import com.codelabs.state.util.generateRandomTodoItem
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class TodoViewModelTest {

    @Test
    fun whenAddItem_updatesList() {
        val viewModel = TodoViewModel()
        val expected = generateRandomTodoItem()
        viewModel.addItem(expected)
        assertThat(viewModel.todoItems).isEqualTo(listOf(expected))
    }

    @Test
    fun whenRemovingItem_updateList() {
        val viewModel = TodoViewModel()
        val item1 = generateRandomTodoItem()
        val item2 = generateRandomTodoItem()
        viewModel.addItem(item1)
        viewModel.addItem(item2)
        viewModel.removeItem(item1)
        assertThat(viewModel.todoItems).isEqualTo(listOf(item2))
    }

    @Test
    fun whenNotEditing_currentEditItemIsNull() {
        val viewModel = TodoViewModel()
        val item = generateRandomTodoItem()
        viewModel.addItem(item)
        assertThat(viewModel.currentEditItem).isNull()
    }

    @Test
    fun whenEditing_currentEditItemIsCorrectItem() {
        val viewModel = TodoViewModel()
        val item1 = generateRandomTodoItem()
        val item2 = generateRandomTodoItem()
        viewModel.addItem(item1)
        viewModel.addItem(item2)
        viewModel.onEditItemSelected(item1)
        assertThat(viewModel.currentEditItem).isEqualTo(item1)
    }

    @Test
    fun whenEditingDone_currentEditItemIsCorrectItem() {
        val viewModel = TodoViewModel()
        val item1 = generateRandomTodoItem()
        val item2 = generateRandomTodoItem()
        viewModel.addItem(item1)
        viewModel.addItem(item2)
        viewModel.onEditItemSelected(item1)
        viewModel.onEditDone()
        assertThat(viewModel.currentEditItem).isNull()
    }

    @Test
    fun whenEditingItem_updatesAreShownInItemAndList() {
        val viewModel = TodoViewModel()
        val item1 = generateRandomTodoItem()
        val item2 = generateRandomTodoItem()
        viewModel.addItem(item1)
        viewModel.addItem(item2)
        viewModel.onEditItemSelected(item1)

        val expected = item1.copy(task = "Update for test case")
        viewModel.onEditItemChange(expected)

        println("expected : $expected")
        println("item1 : $item1")
        println("item2 : $item2")
        assertThat(viewModel.todoItems).isEqualTo(listOf(expected, item2))
        assertThat(viewModel.currentEditItem).isEqualTo(expected)
    }

    @Test(expected = IllegalArgumentException::class)
    fun whenEditing_wrongItemThrows() {
        val viewModel = TodoViewModel()
        val item1 = generateRandomTodoItem()
        val item2 = generateRandomTodoItem()
        viewModel.addItem(item1)
        viewModel.addItem(item2)
        viewModel.onEditItemSelected(item1)
        val expected = item2.copy(task = "Update for test case")
        viewModel.onEditItemChange(expected)

        assertThat(viewModel.todoItems).isNotEqualTo(listOf(expected, item2))
    }

    @Test(expected = IllegalArgumentException::class)
    fun whenNotEditing_onEditItemChangeThrows() {
        val viewModel = TodoViewModel()
        val item = generateRandomTodoItem()
        viewModel.onEditItemChange(item)
    }
}
