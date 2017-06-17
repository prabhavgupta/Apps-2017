using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ServiceLogicLayer.DTO
{
    public class ProfileRatingDTO
    {
        public double AverageClassRating { get; set; }
        public int TotalClassRating{ get; set; }
        public int TotalOlympiadsRating{ get; set; }
        public int MaxOlympiadsRating{ get; set; }
        public int TotalScientificRating{ get; set; }
        public int MaxScientificRating{ get; set; }
        public int TotalSportRating{ get; set; }
        public int MaxSportRating{ get; set; }
        public int TotalArtRating{ get; set; }
        public int MaxArtRating{ get; set; }
        public int TotalSocietyRating{ get; set; }
        public int MaxSocietyRating{ get; set; }
    }
}
