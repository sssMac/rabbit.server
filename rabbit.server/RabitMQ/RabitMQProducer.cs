using Newtonsoft.Json;
using RabbitMQ.Client;
using System.Text;

namespace rabbit.server.RabitMQ
{
	public class RabitMQProducer : IRabitMQProducer
	{
		ConnectionFactory _factory;

		public RabitMQProducer()
		{
			_factory = new ConnectionFactory
			{
				UserName = "guest",
				Password = "guest",
				HostName = "192.168.3.49"
			};
		}

		public void SendLoginMessage<T>(T message)
		{
			var connection = _factory.CreateConnection();
			using var channel = connection.CreateModel();
			channel.QueueDeclare("login", exclusive: false);

			var json = JsonConvert.SerializeObject(message);
			var body = Encoding.UTF8.GetBytes(json);

			channel.BasicPublish(exchange: "", routingKey: "login", body: body);
			Console.WriteLine($"{json} is login!");

		}

		public void SendLogoutMessage(string userName)
		{
			var connection = _factory.CreateConnection();
			using var channel = connection.CreateModel();
			channel.QueueDeclare("logout", exclusive: false);

			var json = JsonConvert.SerializeObject(userName);
			var body = Encoding.UTF8.GetBytes(json);

			channel.BasicPublish(exchange: "", routingKey: "logout", body: body);
			Console.WriteLine($"{userName} was logout!");
		}

		public void InfinitySend()
		{
			var connection = _factory.CreateConnection();
			using var channel = connection.CreateModel();
			channel.QueueDeclare("infinity", exclusive: false);

			string message = "ТЫ ЧМО!";
			var body = Encoding.UTF8.GetBytes(message);

			while (true)
			{
				Thread.Sleep(1000);
				channel.BasicPublish(exchange: "", routingKey: "infinity", body: body);
				Console.WriteLine(message);
			}
			
		}
	}
}
