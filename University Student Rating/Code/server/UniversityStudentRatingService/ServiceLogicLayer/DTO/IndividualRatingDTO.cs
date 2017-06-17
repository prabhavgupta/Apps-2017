using DataAccessLayer.DbContext;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ServiceLogicLayer.DTO
{
    public class IndividualRatingDTO
    {
        public UserProfile UserProfile { get; set; }
        public Discipline Discipline { get; set; }
        public double AverageClassRating { get; set; }
        public int TotalClassRating { get; set; }
        public int TotalScippingClassRating { get; set; }
        public int TotalOlympiadsRating { get; set; }
        public List<Rating> ClassRatings { get; set; }
        public List<Rating> ScippingClassRatings { get; set; }
        public List<Rating> OlympiadsRatings { get; set; }
    }
}
