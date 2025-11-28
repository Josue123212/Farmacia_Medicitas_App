package com.example.farmacia_medicitas.data.local.db

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS products (
                id TEXT NOT NULL PRIMARY KEY,
                name TEXT NOT NULL,
                description TEXT NOT NULL,
                price REAL NOT NULL,
                imageUrl TEXT,
                inStock INTEGER NOT NULL
            );
            """
        )
        db.execSQL("CREATE INDEX IF NOT EXISTS index_products_name ON products(name)")
    }
}

val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE IF NOT EXISTS cart_items_new (productId TEXT NOT NULL PRIMARY KEY, quantity INTEGER NOT NULL)"
        )
        db.execSQL(
            "INSERT INTO cart_items_new(productId, quantity) SELECT productId, quantity FROM cart_items"
        )
        db.execSQL("DROP TABLE cart_items")
        db.execSQL("ALTER TABLE cart_items_new RENAME TO cart_items")
        db.execSQL("DROP INDEX IF EXISTS idx_products_name")
        db.execSQL("CREATE INDEX IF NOT EXISTS index_products_name ON products(name)")
    }
}
