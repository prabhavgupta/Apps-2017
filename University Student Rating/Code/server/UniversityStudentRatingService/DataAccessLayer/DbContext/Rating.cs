namespace DataAccessLayer.DbContext
{
    using System;
    using System.Collections.Generic;
    using System.ComponentModel.DataAnnotations;
    using System.ComponentModel.DataAnnotations.Schema;
    using System.Data.Entity.Spatial;

    [Table("Rating")]
    public partial class Rating
    {
        public int Id { get; set; }

        public string Name { get; set; }

        public int Points { get; set; }

        [Column(TypeName = "date")]
        public DateTime Date { get; set; }

        public int CourseId { get; set; }

        public int DisciplineId { get; set; }

        public int? StudentId { get; set; }

        public int? ProfessorId { get; set; }

        public int? RatingDivisionId { get; set; }

        public virtual Course Course { get; set; }

        public virtual Discipline Discipline { get; set; }

        public virtual RatingDivision RatingDivision { get; set; }

        public  UserProfile UserProfile { get; set; }

        public  UserProfile UserProfile1 { get; set; }
    }
}
