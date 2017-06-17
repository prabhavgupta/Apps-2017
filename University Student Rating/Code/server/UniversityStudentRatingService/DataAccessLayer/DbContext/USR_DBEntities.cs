namespace DataAccessLayer.DbContext
{
    using System;
    using System.Data.Entity;
    using System.ComponentModel.DataAnnotations.Schema;
    using System.Linq;
    using Microsoft.AspNet.Identity.EntityFramework;
    using DataAccessLayer.Entities;
    using System.Data.Entity.Infrastructure;

    public partial class USR_DBEntities : IdentityDbContext<ApplicationUser>
    {
        public USR_DBEntities()
            : base("name=USR_DBEntities")
        {
            ((IObjectContextAdapter)this).ObjectContext.CommandTimeout = 600;
        }

        public static USR_DBEntities Create()
        {
            var db = new USR_DBEntities();
            return db;
        }

        public virtual DbSet<City> Cities { get; set; }
        public virtual DbSet<Country> Countries { get; set; }
        public virtual DbSet<Course> Courses { get; set; }
        public virtual DbSet<Discipline> Disciplines { get; set; }
        public virtual DbSet<DisciplineBranch> DisciplineBranches { get; set; }
        public virtual DbSet<Faculty> Faculties { get; set; }
        public virtual DbSet<GenderType> GenderTypes { get; set; }
        public virtual DbSet<ProfessorDiscipline> ProfessorDisciplines { get; set; }
        public virtual DbSet<Rating> Ratings { get; set; }
        public virtual DbSet<RatingDivision> RatingDivisions { get; set; }
        public virtual DbSet<RatingSystem> RatingSystems { get; set; }
        public virtual DbSet<RatingType> RatingTypes { get; set; }
        public virtual DbSet<Status> Status { get; set; }
        public virtual DbSet<StudentProfessorDiscipline> StudentProfessorDisciplines { get; set; }
        public virtual DbSet<University> Universities { get; set; }
        public virtual DbSet<UserProfile> UserProfiles { get; set; }

        protected override void OnModelCreating(DbModelBuilder modelBuilder)
        {
            base.OnModelCreating(modelBuilder);

            modelBuilder.Entity<City>()
                .HasMany(e => e.Universities)
                .WithRequired(e => e.City)
                .WillCascadeOnDelete(false);

            modelBuilder.Entity<City>()
                .HasMany(e => e.UserProfiles)
                .WithRequired(e => e.City)
                .WillCascadeOnDelete(false);

            modelBuilder.Entity<Country>()
                .HasMany(e => e.Cities)
                .WithRequired(e => e.Country)
                .WillCascadeOnDelete(false);

            modelBuilder.Entity<Country>()
                .HasMany(e => e.UserProfiles)
                .WithRequired(e => e.Country)
                .WillCascadeOnDelete(false);

            modelBuilder.Entity<Course>()
                .HasMany(e => e.Ratings)
                .WithRequired(e => e.Course)
                .WillCascadeOnDelete(false);

            modelBuilder.Entity<Discipline>()
                .HasMany(e => e.ProfessorDisciplines)
                .WithRequired(e => e.Discipline)
                .WillCascadeOnDelete(false);

            modelBuilder.Entity<Discipline>()
                .HasMany(e => e.Ratings)
                .WithRequired(e => e.Discipline)
                .WillCascadeOnDelete(false);

            modelBuilder.Entity<Discipline>()
                .HasMany(e => e.RatingSystems)
                .WithRequired(e => e.Discipline)
                .WillCascadeOnDelete(false);

            modelBuilder.Entity<DisciplineBranch>()
                .HasMany(e => e.Disciplines)
                .WithOptional(e => e.DisciplineBranch)
                .HasForeignKey(e => e.BranchId);

            modelBuilder.Entity<GenderType>()
                .HasMany(e => e.UserProfiles)
                .WithRequired(e => e.GenderType)
                .WillCascadeOnDelete(false);

            modelBuilder.Entity<ProfessorDiscipline>()
                .HasMany(e => e.StudentProfessorDisciplines)
                .WithRequired(e => e.ProfessorDiscipline)
                .WillCascadeOnDelete(false);

            modelBuilder.Entity<RatingType>()
                .HasMany(e => e.RatingSystems)
                .WithRequired(e => e.RatingType)
                .WillCascadeOnDelete(false);

            modelBuilder.Entity<Status>()
                .HasMany(e => e.UserProfiles)
                .WithRequired(e => e.Status)
                .WillCascadeOnDelete(false);

            modelBuilder.Entity<University>()
                .HasMany(e => e.Faculties)
                .WithRequired(e => e.University)
                .WillCascadeOnDelete(false);

            modelBuilder.Entity<University>()
                .HasMany(e => e.RatingSystems)
                .WithRequired(e => e.University)
                .WillCascadeOnDelete(false);

            modelBuilder.Entity<University>()
                .HasMany(e => e.UserProfiles)
                .WithRequired(e => e.University)
                .WillCascadeOnDelete(false);

            modelBuilder.Entity<UserProfile>()
                .HasMany(e => e.ProfessorDisciplines)
                .WithRequired(e => e.UserProfile)
                .HasForeignKey(e => e.ProfessorId)
                .WillCascadeOnDelete(false);

            modelBuilder.Entity<UserProfile>()
                .HasMany(e => e.Ratings)
                .WithOptional(e => e.UserProfile)
                .HasForeignKey(e => e.StudentId);

            modelBuilder.Entity<UserProfile>()
                .HasMany(e => e.Ratings1)
                .WithOptional(e => e.UserProfile1)
                .HasForeignKey(e => e.ProfessorId);

            modelBuilder.Entity<UserProfile>()
                .HasMany(e => e.StudentProfessorDisciplines)
                .WithRequired(e => e.UserProfile)
                .HasForeignKey(e => e.StudentId)
                .WillCascadeOnDelete(false);
        }
    }
}
