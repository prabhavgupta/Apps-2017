using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ServiceLogicLayer.Models
{
    public class SetRatingModel
    {
        public int StudentId { get; set; }
        public int RatingDivisionId { get; set; }
        public int DivisionId { get; set; }
        public int Points { get; set; }
        public string Description { get; set; }
    }
}
