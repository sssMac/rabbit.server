namespace rabbit.server.RabitMQ
{
	public interface IRabitMQProducer
	{
		public void SendLoginMessage<T>(T message);
		public void SendLogoutMessage(string userName);
		public void InfinitySend();
	}
}