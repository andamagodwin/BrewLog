package app.andama.brewlog.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import app.andama.brewlog.data.local.BrewLogDao
import app.andama.brewlog.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var dao: BrewLogDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dao = BrewLogDao(this)
        BrewLogSeedData.seedIfNeeded(dao)

        binding.buttonLogin.setOnClickListener {
            val username = binding.editTextUsername.text?.toString().orEmpty().trim()
            val password = binding.editTextPassword.text?.toString().orEmpty()

            binding.textInputLayoutUsername.error = null
            binding.textInputLayoutPassword.error = null

            if (username.isBlank()) {
                binding.textInputLayoutUsername.error = "Username is required"
                return@setOnClickListener
            }

            if (password.isBlank()) {
                binding.textInputLayoutPassword.error = "Password is required"
                return@setOnClickListener
            }

            val employee = dao.authenticateEmployee(username, password)
            if (employee != null) {
                val intent = Intent(this, ProductListActivity::class.java).apply {
                    putExtra(BrewLogIntents.EXTRA_EMPLOYEE_NAME, employee.fullName)
                }
                startActivity(intent)
                finish()
            } else {
                binding.textInputLayoutPassword.error = "Invalid credentials"
                Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show()
            }
        }
    }
}