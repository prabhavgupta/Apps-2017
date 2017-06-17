namespace DataAccessLayer.DbContext
{
    using System;
    using System.Collections.Generic;
    using System.ComponentModel.DataAnnotations;
    using System.ComponentModel.DataAnnotations.Schema;
    using System.Data.Entity.Spatial;

    [Table("RatingSystem")]
    public partial class RatingSystem
    {
        public int Id { get; set; }

        public int DisciplineId { get; set; }

        public int UniversityId { get; set; }

        public int RatingTypeId { get; set; }

        public  Discipline Discipline { get; set; }

        public  RatingType RatingType { get; set; }

        public  University University { get; set; }
    }
}
