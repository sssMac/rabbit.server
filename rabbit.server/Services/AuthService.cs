using Microsoft.AspNetCore.Mvc;
using rabbit.server.Models;
using rabbit.server.RabitMQ;

namespace rabbit.server.Services
{
	public class AuthService
	{
		private readonly IRabitMQProducer _rabitMQProducer;
		private List<User> Users;

		bool one = true;
		public AuthService(IRabitMQProducer rabitMQProducer)
		{
			_rabitMQProducer = rabitMQProducer;
			Users = new List<User>();
		}

		public async Task<bool> Login(string username, string password)
		{
			if (Users.Any(u => u.Username == username))
				await logout(username);

			var newUser = new User { Username = username, Password = password, Token = Guid.NewGuid() };
			Users.Add(newUser);

			_rabitMQProducer.SendLoginMessage(newUser);

			if (one)
			{
				one = false;
				Task.Factory.StartNew(() => _rabitMQProducer.InfinitySend());
			}

			return true;
		}

		public async Task<bool> logout(string username)
		{
			if (!Users.Any(u => u.Username == username))
				return false;

			var user = Users.FirstOrDefault(u => u.Username == username);
			Users.Remove(user);

			_rabitMQProducer.SendLogoutMessage($"{username}");
			return true;
		}

		public async Task<bool> CheckAuth(Guid token)
		{
			if (Users.Any(u => u.Token == token))
				return true;

			return false;
		}
	}
}
