package com.neco_desarrollo.shoppinglist.db

import androidx.lifecycle.*
import com.neco_desarrollo.shoppinglist.entities.LibraryItem
import com.neco_desarrollo.shoppinglist.entities.NoteItem
import com.neco_desarrollo.shoppinglist.entities.ShopListItem
import com.neco_desarrollo.shoppinglist.entities.ShopListNameItem
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class MainViewModel(database: MainDataBase) : ViewModel() {
    val dao = database.getDao()
    val libraryItems = MutableLiveData<List<LibraryItem>>()
    val allNotes: LiveData<List<NoteItem>> = dao.getAllNotes().asLiveData()
    val allShopListNamesItem: LiveData<List<ShopListNameItem>> = dao.getAllShopListNames().asLiveData()
    fun getAllItemsFromList(listId: Int): LiveData<List<ShopListItem>>{
        return dao.getAllShopListItems(listId).asLiveData()
    }
    fun getAllLibraryItems(name: String) = viewModelScope.launch {
         libraryItems.postValue(dao.getAllLibraryItems(name))
    }
    fun insertNote(note: NoteItem) = viewModelScope.launch {
        dao.insertNote(note)
    }
    fun insertShopListName(listNameItem: ShopListNameItem) = viewModelScope.launch {
        dao.insertShopListName(listNameItem)
    }
    fun insertShopItem(shopListItem: ShopListItem) = viewModelScope.launch {
        dao.insertItem(shopListItem)
        if(!isLibraryItemExists(shopListItem.name)) dao.insertLibraryItem(LibraryItem(null, shopListItem.name))
    }
    fun updateListItem(item: ShopListItem) = viewModelScope.launch {
        dao.updateListItem(item)
    }
    fun updateNote(note: NoteItem) = viewModelScope.launch {
        dao.updateNote(note)
    }
    fun updateLibraryItem(item: LibraryItem) = viewModelScope.launch {
        dao.updateLibraryItem(item)
    }
    fun updateListName(shopListNameItem: ShopListNameItem) = viewModelScope.launch {
        dao.updateListName(shopListNameItem)
    }
    fun deleteNote(id: Int) = viewModelScope.launch {
        dao.deleteNote(id)
    }
    fun deleteLibraryItem(id: Int) = viewModelScope.launch {
        dao.deleteLibraryItem(id)
    }
    fun deleteShopList(id: Int, deleteList: Boolean) = viewModelScope.launch {
        if(deleteList)dao.deleteShopListName(id)
        dao.deleteShopItemsByListId(id)
    }
    private suspend fun isLibraryItemExists(name: String): Boolean{
        return dao.getAllLibraryItems(name).isNotEmpty()
    }


  class MainViewModelFactory(val database: MainDataBase) : ViewModelProvider.Factory{
      override fun <T : ViewModel?> create(modelClass: Class<T>): T {
          if(modelClass.isAssignableFrom(MainViewModel::class.java)){
              @Suppress("UNCHECKED_CAST")
             return MainViewModel(database) as T
          }
          throw IllegalArgumentException("Unknown ViewModelClass")
      }
  }
}