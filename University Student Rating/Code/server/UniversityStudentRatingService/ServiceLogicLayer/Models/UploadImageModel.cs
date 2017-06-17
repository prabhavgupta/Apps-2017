using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ServiceLogicLayer.Models
{
    public class UploadImageModel
    {
        public string Base64Image { get; set; }
        public string FilePath { get; set; }
    }
}
