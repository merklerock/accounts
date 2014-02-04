package persistence 

import net.fwbrasil.activate.ActivateContext
import net.fwbrasil.activate.storage.memory.TransientMemoryStorage
import net.fwbrasil.activate.storage.relational.async.AsyncPostgreSQLStorage
import com.github.mauricio.async.db.postgresql.pool.PostgreSQLConnectionFactory
import com.github.mauricio.async.db.Configuration

object computerPersistenceContext extends ActivateContext {

    val storage = new AsyncPostgreSQLStorage {
        def configuration =
            new Configuration(
                username = "account-app",
                host = "pgsql.db.services.merklerock.com",
                password = Some("postgres"),
                database = Some("accounts"))
        lazy val objectFactory = new PostgreSQLConnectionFactory(configuration)
    }

}

