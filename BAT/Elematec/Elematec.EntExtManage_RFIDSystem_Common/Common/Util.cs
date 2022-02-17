using System.IO;
using System.Reflection;

namespace Elematec.EntExtManage_RFIDSystem_Common.Common
{
    internal class Util
    {
        internal static string GetCurrentDirectory()
        {
            string fqn = Assembly.GetExecutingAssembly().ManifestModule.FullyQualifiedName;
            var finfo = new FileInfo(fqn);
            return finfo.DirectoryName;
        }
    }
}
