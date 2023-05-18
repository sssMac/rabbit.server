using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using rabbit.server.Models;
using rabbit.server.RabitMQ;
using rabbit.server.Services;

namespace rabbit.server.Controllers
{
	[Route("api/[controller]")]
	[ApiController]
	public class HomeController : ControllerBase
	{
		private AuthService _authService;

		public HomeController(AuthService authService)
		{
			_authService = authService;
		}

		[HttpPost("login")]
		public async Task<IActionResult> Login(string username, string password)
		{
			if (await _authService.Login(username, password))
			{
				return Ok(new User { Username = username, Password= password });
			}

			return BadRequest("User is already!");
		}

		[HttpPost("logout")]
		public async Task<IActionResult> logout(string username)
		{
			if (await _authService.logout(username))
			{
				return Ok("LOG OUT!");
			}

			return BadRequest("USER NOT FOUND");
		}

	}
}
