using DataAccessLayer.DbContext;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ServiceLogicLayer.DTO
{
    public class ConcurentResultDTO
    {
        public UserProfile UserProfile { get; set; }
        public double RatingValue { get; set; }
    }
}
