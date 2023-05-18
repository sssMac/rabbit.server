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

		[HttpGet("login")]
		public async Task<IActionResult> Login(string username, string password)
		{
			if (await _authService.Login(username, password))
			{
				return Ok(new User { Username = username, Password= password });
			}

			return BadRequest("User is already!");
		}

		[HttpGet("logout")]
		public async Task<IActionResult> logout(string username)
		{
			if (await _authService.logout(username))
			{
				return Ok("LOG OUT!");
			}

			return BadRequest("USER NOT FOUND");
		}

		[HttpGet("CheckAuth")]
		public async Task<IActionResult> CheckAuth(Guid token)
		{
			if (await _authService.CheckAuth(token))
			{
				return Ok("YOU AUTH!");
			}

			return BadRequest("YOU LOGOUT!");
		}

	}
}
