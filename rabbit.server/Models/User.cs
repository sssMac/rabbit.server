namespace rabbit.server.Models
{
	public class User
	{
		public string Username { get; set; }
		public string Password { get; set; }

		public Guid Token { get; set; }
	}
}
