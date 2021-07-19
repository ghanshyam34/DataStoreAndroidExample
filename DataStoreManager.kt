class DataStoreManager{
    companion object {
        var dataStore:DataStore<Preferences>? = null
        private var instance: DataStoreManager? = null
        fun getInstance(context: Context): DataStoreManager {
            if (instance == null) {
                dataStore = context.createDataStore("pref")
                instance = DataStoreManager()
                return instance as DataStoreManager

            } else {
                return instance as DataStoreManager
            }
        }
    }

    fun<T> set(preferencesKey: PreferencesKeys, value: T) {
        GlobalScope.launch {
            dataStore!!.edit { settings ->
             if (value is Boolean) {
                    settings[preferencesKey.getKey()]= (value as Boolean?)!!
                } else if (value is Int) {
                    settings[preferencesKey.getKey()] = (value as Int?)!!
                } else if (value is Float) {
                    settings[preferencesKey.getKey()]= (value as Float?)!!
                } else if (value is Long) {
                    settings[preferencesKey.getKey()] = (value as Long?)!!
                } else  if (value is String) {
                    settings[preferencesKey.getKey()]= (value as String?)!!
                } else if (value is Enum<*>) {
                    settings[preferencesKey.getKey()]=  value.toString()
                } else if (value != null) {
                    throw RuntimeException("Attempting to save non-supported preference")
                }
            }
        }
    }


    fun<T> get(key: PreferencesKeys, defValue: Any): T {
        val value = runBlocking {
            dataStore!!.data.first().get(key.getKey<T>())
        }
        return value as T
    }

    fun has(key: PreferencesKeys): Flow<Boolean> {
        return dataStore!!.data.map{
            it.contains(key.keys)
        }
    }

    suspend fun clearSharedPreference() {
        GlobalScope.launch {
            dataStore!!.edit { settings ->
                settings.clear()
            }
        }
    }

    fun addValue(preferencesKey: PreferencesKeys, value: String) {
        set(preferencesKey,value)
    }

    fun getValue(key: PreferencesKeys): String {
        return get(key,"") as String
    }

    fun addBoolean(preferencesKey: PreferencesKeys, value: Boolean) {
        set(preferencesKey,value)
    }

    fun getBoolean(key: PreferencesKeys): Boolean {
        val v = get(key,false) as Boolean
        return  v
    }

    suspend fun addInt(preferencesKey: PreferencesKeys, value: Int) {
        set(preferencesKey,value)
    }

    fun getInt(key: PreferencesKeys): Int {
        return get(key,0) as Int
    }

    suspend fun addFloat(preferencesKey: PreferencesKeys, value: Float) {
        set(preferencesKey,value)
    }

    fun getFloat(key: PreferencesKeys): Float {
        return get(key, 0f) as Float
    }
}
