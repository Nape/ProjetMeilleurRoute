using System;
using System.IO;
using System.Net;
using System.Text;

namespace CSharpApp
{
    class MainClass
    {
        public static void Main(string[] args)
        {
            Console.OutputEncoding = Encoding.UTF8;
            string url = "http://localhost:4444/Serveur_REST_TP4_war/route/";

            Console.WriteLine("Taper votre nom de livreur");
            string nomLivreur = Console.ReadLine();
            url += nomLivreur;


            HttpWebRequest request = (HttpWebRequest)WebRequest.Create(url);
            if (request != null)
            {
                request.Method = "GET";
            }

            WebResponse webResponse = request.GetResponse();
            Stream webStream = webResponse.GetResponseStream();
            StreamReader responseReader = new StreamReader(webStream);

            string response = responseReader.ReadToEnd();
            if (string.IsNullOrEmpty(response))
            {
                Console.WriteLine("Aucune route disponible.");
            }
            else
            {
                Console.WriteLine("Response: " + "\n" + explode(response));
            }
            responseReader.Close();

        }

        private static String explode(string maString)
        {

            String[] split = maString.Split('&');
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < split.Length; i++)
            {
                sb.Append(split[i]);
                if (i != split.Length - 1)
                {
                    sb.Append(" ");
                }
            }
            return sb.ToString();
        }
    }
}
